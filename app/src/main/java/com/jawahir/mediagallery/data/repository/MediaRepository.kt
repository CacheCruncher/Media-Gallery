package com.jawahir.mediagallery.data.repository

import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.data.model.AlbumModel
import com.jawahir.mediagallery.data.model.MediaModel
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getImageAlbums(): Flow<MediaResult<List<AlbumModel>>>
    fun getImagesInAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>>
    fun getVideoAlbums(): Flow<MediaResult<List<AlbumModel>>>
    fun getVideosInAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>>
}