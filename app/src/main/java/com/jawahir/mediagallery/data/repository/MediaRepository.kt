package com.jawahir.mediagallery.data.repository

import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.model.AlbumModel
import com.jawahir.mediagallery.data.model.MediaModel
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getAlbums(): Flow<MediaResult<List<AlbumModel>>>
    fun getMediaByAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>>
}