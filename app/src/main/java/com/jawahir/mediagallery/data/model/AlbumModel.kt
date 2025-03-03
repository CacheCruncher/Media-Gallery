package com.jawahir.multimediagallery.database.models

import android.net.Uri

data class AlbumModel(
    val albumId: Long,
    val albumName: String,
    val albumThumbnailUri: Uri,
    val albumLastModified: Long,
    val albumMediaType: MediaType,
    var albumMediaCount: Int
)