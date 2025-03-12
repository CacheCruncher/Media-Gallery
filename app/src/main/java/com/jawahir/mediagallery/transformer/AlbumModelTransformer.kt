package com.jawahir.mediagallery.transformer

import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.repository.MediaRepository
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumModelTransformer @Inject constructor(private val repository: MediaRepository) {
    fun getAllAlbums(): Flow<MediaResult<List<AlbumUIModel>>> {
        return repository.getAlbums().map { result ->
            when (result) {
                is MediaResult.Loading ->
                    MediaResult.Loading(data = result.data?.map { AlbumUIModel(it) } ?: emptyList())

                is MediaResult.Success -> {
                    MediaResult.Success(result.data?.map { AlbumUIModel(it) } ?: emptyList())
                }

                is MediaResult.Error ->
                    MediaResult.Error(error = result.error ?: Throwable(message = "Unknown error"))
            }

        }
    }
}