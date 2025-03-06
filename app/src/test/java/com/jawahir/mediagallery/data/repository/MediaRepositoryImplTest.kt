package com.jawahir.mediagallery.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.jawahir.mediagallery.data.MediaResult
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.Mockito.isNull
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MediaStore.Files::class, ContentUris::class, Uri::class)
class MediaRepositoryImplTest {

    private lateinit var mediaRepository: MediaRepositoryImpl
    private lateinit var contentResolver: ContentResolver

    @Mock
    private lateinit var cursor: Cursor

    private lateinit var mockedUri: Uri

    @Before
    fun setUp() {
        val context = mock(Context::class.java)
        contentResolver = mock(ContentResolver::class.java)
        `when`(context.contentResolver).thenReturn(contentResolver)
        mediaRepository = MediaRepositoryImpl(context)

        PowerMockito.mockStatic(Uri::class.java)
        PowerMockito.mockStatic(ContentUris::class.java)
        PowerMockito.mockStatic(MediaStore.Files::class.java)

        mockedUri = mock(Uri::class.java)
        PowerMockito.`when`(Uri.parse("content://media/external/images/media/101L"))
            .thenReturn(mockedUri)
        `when`(
            ContentUris.withAppendedId(
                eq(MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                eq(101L)
            )
        ).thenReturn(mockedUri)
        PowerMockito.`when`(MediaStore.Files.getContentUri("external")).thenReturn(mockedUri)
        `when`(cursor.toString()).thenReturn("Mock Cursor")
    }

    @Test
    fun `getAlbums emits Error when cursor is null`() = runTest {
        `when`(contentResolver.query(any(), any(), isNull(), isNull(), any())).thenReturn(null)

        val result = mediaRepository.getAlbums().toList()

        assertEquals(2, result.size)
        assertTrue(result[0] is MediaResult.Loading)
        assertTrue(result[1] is MediaResult.Error)
    }

    @Test
    fun `getAlbums emits Success with album list when cursor has data`() = runTest {
        val imageThumbnailUri = mock(Uri::class.java)
        val videoThumbnailUri = mock(Uri::class.java)

        `when`(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(cursor)

        `when`(cursor.count).thenReturn(1)
        `when`(cursor.moveToNext()).thenReturn(true).thenReturn(false)
        `when`(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)).thenReturn(0)
        `when`(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)).thenReturn(
            1
        )
        `when`(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)).thenReturn(2)
        `when`(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)).thenReturn(3)
        `when`(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)).thenReturn(
            4
        )

        `when`(cursor.getLong(0)).thenReturn(101L)
        `when`(cursor.getString(1)).thenReturn("BucketName")
        `when`(cursor.getLong(2)).thenReturn(201L)
        `when`(cursor.getInt(3)).thenReturn(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        `when`(cursor.getLong(4)).thenReturn(1625097600L)

        PowerMockito.`when`(
            ContentUris.withAppendedId(
                eq(MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                eq(201L)
            )
        ).thenReturn(imageThumbnailUri)
        PowerMockito.`when`(
            ContentUris.withAppendedId(
                eq(MediaStore.Video.Media.EXTERNAL_CONTENT_URI),
                eq(201L)
            )
        ).thenReturn(videoThumbnailUri)

        val result = mediaRepository.getAlbums().toList()

        assertEquals(2, result.size)
        assertTrue(result[0] is MediaResult.Loading)
        assertTrue(result[1] is MediaResult.Success)


        val successResult = result[1] as MediaResult.Success
        assertNotNull(successResult.data)
    }

    @Test
    fun `getMediaByAlbum emits Error when cursor is null`() = runTest {
        `when`(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(null)

        val result = mediaRepository.getMediaByAlbum(1L).toList()

        assertEquals(2, result.size)
        assertTrue(result[1] is MediaResult.Error)
    }

    @Test
    fun `getMediaByAlbum emits Error when cursor is empty`() = runTest {
        `when`(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(cursor)
        `when`(cursor.moveToFirst()).thenReturn(false)

        val result = mediaRepository.getMediaByAlbum(1L).toList()

        assertEquals(2, result.size)
        assertTrue(result[1] is MediaResult.Error)
    }

    @Test
    fun `getMediaByAlbum emits Success with media list when cursor has data`() = runTest {
        val mediaUri = mock(Uri::class.java)
        val thumbnailUri = mock(Uri::class.java)

        `when`(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(cursor)
        `when`(cursor.moveToFirst()).thenReturn(true)
        `when`(cursor.moveToNext()).thenReturn(false)
        `when`(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)).thenReturn(0)
        `when`(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)).thenReturn(1)
        `when`(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)).thenReturn(2)
        `when`(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)).thenReturn(3)
        `when`(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)).thenReturn(4)

        `when`(cursor.getLong(0)).thenReturn(101L)
        `when`(cursor.getString(1)).thenReturn("Test Title")
        `when`(cursor.getString(2)).thenReturn("Test Display Name")
        `when`(cursor.getLong(3)).thenReturn(1625097600L)
        `when`(cursor.getInt(4)).thenReturn(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

        PowerMockito.`when`(
            ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                101L
            )
        ).thenReturn(mediaUri)
        PowerMockito.`when`(ContentUris.withAppendedId(mediaUri, 101L)).thenReturn(thumbnailUri)

        val result = mediaRepository.getMediaByAlbum(1L).toList()

        assertEquals(2, result.size)
        assertTrue(result[1] is MediaResult.Success)

        val successResult = result[1] as MediaResult.Success
        assertNotNull(successResult.data)
    }
}