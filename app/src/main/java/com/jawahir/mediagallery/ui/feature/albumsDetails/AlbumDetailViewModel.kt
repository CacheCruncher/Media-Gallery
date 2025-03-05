package com.jawahir.mediagallery.ui.feature.albumsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.transformer.AlbumDetailModelTransformer
import com.jawahir.mediagallery.ui.uimodels.AlbumDetailUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val transformer: AlbumDetailModelTransformer,
    private val ioDispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private val _state: MutableStateFlow<MediaResult<List<AlbumDetailUIModel>>> =
        MutableStateFlow(MediaResult.Loading(null))
    val state = _state.asStateFlow()

    fun onStart(albumId: Long) {
        viewModelScope.launch(ioDispatcher) {
            transformer.getMediaByAlbum(albumId).collectLatest {
                _state.emit(it)
            }
        }
    }
}