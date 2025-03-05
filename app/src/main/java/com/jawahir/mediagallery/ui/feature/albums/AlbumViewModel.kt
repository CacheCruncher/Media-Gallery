package com.jawahir.mediagallery.ui.feature.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.transformer.AlbumModelTransformer
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val transformer: AlbumModelTransformer,
    private val ioDispatcher: CoroutineDispatcher) : ViewModel() {

    private val _state: MutableStateFlow<MediaResult<List<AlbumUIModel>>> =
        MutableStateFlow(MediaResult.Loading(null))
    val state = _state.asStateFlow()

    fun onStart() {
        viewModelScope.launch(ioDispatcher) {
            transformer.getAllAlbums().collectLatest {
                _state.emit(it)
            }
        }
    }

}