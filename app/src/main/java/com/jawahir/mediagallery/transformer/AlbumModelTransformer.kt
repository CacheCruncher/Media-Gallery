package com.jawahir.mediagallery.transformer

import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import com.jawahir.multimediagallery.database.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumModelTransformer @Inject constructor(private val repository: MediaRepository) {
    fun getAllAlbums(): Flow<MediaResult<List<AlbumUIModel>>> {
        return repository.getImageAlbums().map { result ->
            when (result) {
                is MediaResult.Loading -> MediaResult.Loading()
                is MediaResult.Success -> {
                    val uiModel = result.data?.map { AlbumUIModel(it) } ?: emptyList()
                    MediaResult.Success(uiModel)
                }

                is MediaResult.Error -> MediaResult.Error(
                    error = result.error ?: Throwable(message = "Unknown error")
                )
            }

        }
    }
}