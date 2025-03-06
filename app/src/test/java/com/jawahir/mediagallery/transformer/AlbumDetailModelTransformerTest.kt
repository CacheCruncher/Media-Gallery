package com.jawahir.mediagallery.transformer

import android.net.Uri
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.model.MediaModel
import com.jawahir.mediagallery.data.model.MediaType
import com.jawahir.mediagallery.data.repository.MediaRepository
import com.jawahir.mediagallery.ui.uimodels.AlbumDetailUIModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
class AlbumDetailModelTransformerTest {

    private lateinit var albumDetailModelTransformer: AlbumDetailModelTransformer

    @Mock
    private lateinit var repository: MediaRepository

    @Before
    fun setUp() {
        albumDetailModelTransformer = AlbumDetailModelTransformer(repository)
    }

    @Test
    fun `getMediaByAlbum emits Loading when repository emits Loading`() = runTest {
        `when`(repository.getMediaByAlbum(1L)).thenReturn(flowOf(MediaResult.Loading()))

        val result = albumDetailModelTransformer.getMediaByAlbum(1L).toList()

        assertEquals(1, result.size)
        assertTrue(result[0] is MediaResult.Loading)
    }

    @Test
    fun `getMediaByAlbum emits Success with AlbumDetailUIModel list when repository emits Success with MediaModel list`() =
        runTest {
            val mediaModels = listOf(
                MediaModel(
                    1L,
                    "Title 1",
                    "Name 1",
                    1625097600L,
                    mock(Uri::class.java),
                    mock(Uri::class.java),
                    MediaType.IMAGE
                ),
                MediaModel(
                    2L,
                    "Title 2",
                    "Name 2",
                    1625184000L,
                    mock(Uri::class.java),
                    mock(Uri::class.java),
                    MediaType.VIDEO
                )
            )
            `when`(repository.getMediaByAlbum(1L)).thenReturn(flowOf(MediaResult.Success(mediaModels)))

            val result = albumDetailModelTransformer.getMediaByAlbum(1L).toList()

            assertEquals(1, result.size)
            assertTrue(result[0] is MediaResult.Success)

            val successResult = result[0] as MediaResult.Success
            assertEquals(2, successResult.data?.size)
            assertTrue(successResult.data?.get(0) is AlbumDetailUIModel)
            assertTrue(successResult.data?.get(1) is AlbumDetailUIModel)
        }

    @Test
    fun `getMediaByAlbum emits Success with empty list when repository emits Success with null data`() =
        runTest {
            `when`(repository.getMediaByAlbum(1L)).thenReturn(flowOf(MediaResult.Success(emptyList())))

            val result = albumDetailModelTransformer.getMediaByAlbum(1L).toList()

            assertEquals(1, result.size)
            assertTrue(result[0] is MediaResult.Success)

            val successResult = result[0] as MediaResult.Success
            assertEquals(0, successResult.data?.size)
        }

    @Test
    fun `getMediaByAlbum emits Error when repository emits Error`() = runTest {
        val error = Throwable("Test Error")
        `when`(repository.getMediaByAlbum(1L)).thenReturn(flowOf(MediaResult.Error(null, error)))

        val result = albumDetailModelTransformer.getMediaByAlbum(1L).toList()

        assertEquals(1, result.size)
        assertTrue(result[0] is MediaResult.Error)

        val errorResult = result[0] as MediaResult.Error
        assertEquals(error, errorResult.error)
    }
}