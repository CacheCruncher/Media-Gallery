package com.jawahir.multimediagallery.database.repository

import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.multimediagallery.database.models.AlbumModel
import com.jawahir.multimediagallery.database.models.MediaModel
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getImageAlbums(): Flow<MediaResult<List<AlbumModel>>>
    fun getImagesInAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>>
    fun getVideoAlbums(): Flow<MediaResult<List<AlbumModel>>>
    fun getVideosInAlbum(albumId: Long): Flow<MediaResult<List<MediaModel>>>
}