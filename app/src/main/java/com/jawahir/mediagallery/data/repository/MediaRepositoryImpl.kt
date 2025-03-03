package com.jawahir.mediagallery.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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
    override fun getImageAlbums(): Flow<MediaResult<List<AlbumModel>>> = flow {
        emit(MediaResult.Loading())
        try {
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATE_MODIFIED
                ), null, null, "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
            )?.use { cursor ->
                if (cursor.count == 0) {
                    emit(MediaResult.Error(error = Exception()))
                    return@use
                }

                val albums = mutableMapOf<Long, AlbumModel>()
                var firstThumbnailUri: Uri? = null
                var totalMediaCount = 0

                while (cursor.moveToNext()) {
                    val albumId =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID))
                    val albumName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                            .orEmpty()
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val dateModified =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED))
                    val thumbnailUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    if (firstThumbnailUri == null) {
                        firstThumbnailUri = thumbnailUri
                    }

                    totalMediaCount++

                    albums.computeIfAbsent(albumId) {
                        AlbumModel(
                            albumId = albumId,
                            albumName = albumName,
                            albumLastModified = dateModified,
                            albumThumbnailUri = thumbnailUri,
                            albumMediaType = MediaType.IMAGE,
                            albumMediaCount = 0
                        )
                    }.albumMediaCount++
                }

                val result = mutableListOf<AlbumModel>().apply {
                    firstThumbnailUri?.let {
                        add(
                            AlbumModel(
                                albumId = -1,
                                albumName = "All Images",
                                albumLastModified = System.currentTimeMillis(),
                                albumThumbnailUri = it,
                                albumMediaType = MediaType.IMAGE,
                                albumMediaCount = totalMediaCount
                            )
                        )
                    }
                    addAll(albums.values.sortedBy { it.albumName })
                }

                emit(MediaResult.Success(result))
            } ?: emit(MediaResult.Error(error = Exception()))

        } catch (e: Exception) {
            emit(MediaResult.Error(error = e))
        }
    }

    override fun getImagesInAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>> {
        TODO("Not yet implemented")
    }

    override fun getVideoAlbums(): Flow<MediaResult<List<AlbumModel>>> {
        TODO("Not yet implemented")
    }

    override fun getVideosInAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>> {
        TODO("Not yet implemented")
    }
}