package com.jawahir.mediagallery.ui.feature.albums

import android.net.Uri
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.model.AlbumModel
import com.jawahir.mediagallery.data.model.MediaType
import com.jawahir.mediagallery.transformer.AlbumModelTransformer
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(PowerMockRunner::class)
@PrepareForTest(AlbumModelTransformer::class)
class AlbumViewModelTest {

    private lateinit var viewModel: AlbumViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var transformer: AlbumModelTransformer

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumViewModel(transformer, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onStart emits Loading and then Success when transformer emits Success`() = runTest {
        val albumModel1 =
            AlbumModel(1L, "Album 1", mock(Uri::class.java), 1625097600L, MediaType.IMAGE, 1)
        val albumModel2 =
            AlbumModel(2L, "Album 2", mock(Uri::class.java), 1625184000L, MediaType.VIDEO, 2)

        val uiModels = listOf(
            AlbumUIModel(albumModel1),
            AlbumUIModel(albumModel2)
        )
        PowerMockito.`when`(transformer.getAllAlbums())
            .thenReturn(flowOf(MediaResult.Success(uiModels)))

        val stateValues = mutableListOf<MediaResult<List<AlbumUIModel>>>()
        val job = launch {
            viewModel.state.collect {
                stateValues.add(it)
            }
        }
        viewModel.onStart()
        advanceUntilIdle()

        assertEquals(2, stateValues.size)
        assertTrue(stateValues[0] is MediaResult.Loading)
        assertTrue(stateValues[1] is MediaResult.Success)
        assertEquals(uiModels, (stateValues[1] as MediaResult.Success).data)

        job.cancel()
    }


    @Test
    fun `onStart emits Loading and then Error when transformer emits Error`() = runTest {
        val error = Throwable("Test Error")
        PowerMockito.`when`(transformer.getAllAlbums()).thenReturn(flowOf(MediaResult.Error(error = error)))

        val stateValues = mutableListOf<MediaResult<List<AlbumUIModel>>>()
        val job = launch {
            viewModel.state.collect {
                stateValues.add(it)
            }
        }

        viewModel.onStart()
        advanceUntilIdle()

        assertEquals(2, stateValues.size)
        assertTrue(stateValues[0] is MediaResult.Loading)
        assertTrue(stateValues[1] is MediaResult.Error)
        assertEquals(error, (stateValues[1] as MediaResult.Error).error)

        job.cancel()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val stateValues = mutableListOf<MediaResult<List<AlbumUIModel>>>()
        val job = launch {
            viewModel.state.collect {
                stateValues.add(it)
            }
        }
        advanceUntilIdle()
        assertEquals(1, stateValues.size)
        assertTrue(stateValues[0] is MediaResult.Loading)
        job.cancel()
    }
}