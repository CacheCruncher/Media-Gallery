package com.jawahir.mediagallery.transformer

import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.repository.MediaRepository
import com.jawahir.mediagallery.ui.uimodels.AlbumDetailUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumDetailModelTransformer @Inject constructor(val repository: MediaRepository) {
    fun getMediaByAlbum(albumId: Long): Flow<MediaResult<List<AlbumDetailUIModel>>> {
        return repository.getMediaByAlbum(albumId).map { result ->

            when (result) {
                is MediaResult.Loading -> MediaResult.Loading(data = result.data?.map { AlbumDetailUIModel(it) }?:emptyList())
                is MediaResult.Success -> {
                    val uiModel = result.data?.map { AlbumDetailUIModel(it) } ?: emptyList()
                    MediaResult.Success(uiModel)
                }
                is MediaResult.Error -> MediaResult.Error(error = result.error?:Exception())
            }
        }
    }
}