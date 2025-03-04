package com.jawahir.mediagallery.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.model.AlbumModel
import com.jawahir.mediagallery.data.model.MediaModel
import com.jawahir.mediagallery.data.model.MediaType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    MediaRepository {

   override fun getAlbums(): Flow<MediaResult<List<AlbumModel>>> = flow {
        emit(MediaResult.Loading())
        try {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns.BUCKET_ID,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED
            )
            val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
            val selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
                if (cursor.count == 0) {
                    emit(MediaResult.Error(error = Exception("No media available")))
                    return@use
                }

                val albums = mutableMapOf<Long, AlbumModel>()
                var firstImageUri: Uri? = null
                var firstVideoUri: Uri? = null
                var totalImageCount = 0
                var totalVideoCount = 0

                while (cursor.moveToNext()) {
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID))
                    val albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)).orEmpty()
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                    val mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                    val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED))

                    val contentUri = if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else {
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }

                    val thumbnailUri = ContentUris.withAppendedId(contentUri, id)

                    // Store first image and video for "All Images" & "All Videos" folders
                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                        if (firstImageUri == null) firstImageUri = thumbnailUri
                        totalImageCount++
                    } else {
                        if (firstVideoUri == null) firstVideoUri = thumbnailUri
                        totalVideoCount++
                    }

                    albums.computeIfAbsent(albumId) {
                        AlbumModel(
                            albumId = albumId,
                            albumName = albumName,
                            albumLastModified = dateModified,
                            albumThumbnailUri = thumbnailUri,
                            albumMediaType = if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) MediaType.IMAGE else MediaType.VIDEO,
                            albumMediaCount = 0
                        )
                    }.albumMediaCount++
                }

                val result = mutableListOf<AlbumModel>().apply {
                    // Add "All Images" Album
                    firstImageUri?.let {
                        add(
                            AlbumModel(
                                albumId = -2, // Special ID for all images
                                albumName = "All Images",
                                albumLastModified = System.currentTimeMillis(),
                                albumThumbnailUri = it,
                                albumMediaType = MediaType.IMAGE,
                                albumMediaCount = totalImageCount
                            )
                        )
                    }

                    // Add "All Videos" Album
                    firstVideoUri?.let {
                        add(
                            AlbumModel(
                                albumId = -3, // Special ID for all videos
                                albumName = "All Videos",
                                albumLastModified = System.currentTimeMillis(),
                                albumThumbnailUri = it,
                                albumMediaType = MediaType.VIDEO,
                                albumMediaCount = totalVideoCount
                            )
                        )
                    }

                    // Add normal albums
                    addAll(albums.values.sortedBy { it.albumLastModified })
                }

                emit(MediaResult.Success(result))
            } ?: emit(MediaResult.Error(error = Exception()))

        } catch (e: Exception) {
            emit(MediaResult.Error(error = e))
        }
    }


    override fun getMediaByAlbum(mediaId: Long): Flow<MediaResult<List<MediaModel>>> = flow {
        emit(MediaResult.Loading())
        try {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.BUCKET_ID // Add BUCKET_ID to projection
            )

            val selection: String
            val selectionArgs: Array<String>

            when (mediaId) {
                -2L -> { // Fetch all images
                    selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? AND ${MediaStore.Files.FileColumns.SIZE} > 0"
                    selectionArgs = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                }
                -3L -> { // Fetch all videos
                    selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? AND ${MediaStore.Files.FileColumns.SIZE} > 0"
                    selectionArgs = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                }
                else -> { // Fetch media from a specific album
                    selection = "${MediaStore.Files.FileColumns.BUCKET_ID} = ? AND ${MediaStore.Files.FileColumns.SIZE} > 0"
                    selectionArgs = arrayOf(mediaId.toString())
                }
            }

            val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
                ?.use { cursor ->
                    if (!cursor.moveToFirst()) {
                        emit(MediaResult.Error(error = Exception("No media available")))
                        return@use
                    }

                    val idIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                    val titleIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)
                    val displayNameIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    val dateModifiedIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
                    val mediaTypeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)

                    val mediaList = mutableListOf<MediaModel>()

                    do {
                        try {
                            val id = cursor.getLong(idIndex)
                            val title = cursor.getString(titleIndex).orEmpty()
                            val displayName = cursor.getString(displayNameIndex).orEmpty()
                            val dateModified = cursor.getLong(dateModifiedIndex)
                            val mediaTypeInt = cursor.getInt(mediaTypeIndex)

                            val mediaType = if (mediaTypeInt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) MediaType.VIDEO else MediaType.IMAGE
                            val contentUri = if (mediaType == MediaType.VIDEO) {
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            } else {
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            val mediaUri = ContentUris.withAppendedId(contentUri, id)

                            //val thumbnailUri = getThumbnailUri(context.contentResolver, mediaType, id)
                            val thumbnailUri = ContentUris.withAppendedId(mediaUri, id)
                            mediaList.add(
                                MediaModel(
                                    id = id,
                                    mediaTitle = title,
                                    mediaName = displayName,
                                    mediaLastModified = dateModified,
                                    mediaUri = mediaUri,
                                    mediaThumbnailUri = thumbnailUri,
                                    mediaType = mediaType
                                )
                            )
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    } while (cursor.moveToNext())

                    emit(MediaResult.Success(mediaList.sortedBy { it.mediaLastModified }))
                } ?: emit(MediaResult.Error(error = Exception()))

        } catch (e: Throwable) {
            emit(MediaResult.Error(error = e))
        }
    }

    private fun getThumbnailUri(contentResolver: ContentResolver, mediaType: MediaType, id: Long): Uri? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentUri = when (mediaType) {
                    MediaType.VIDEO -> ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    MediaType.IMAGE -> ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                }
                contentResolver.loadThumbnail(contentUri, Size(512, 512), null)
                    .let {
                        Uri.parse("content://media/external/file/${id}") //using the original ID and building the uri.
                    }
            } else {
                if (mediaType == MediaType.VIDEO) {
                    ContentUris.withAppendedId(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, id)
                } else {
                    ContentUris.withAppendedId(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, id)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



}