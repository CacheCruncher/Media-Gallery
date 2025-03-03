package com.jawahir.multimediagallery.database.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumModel(
    val albumId: Long,
    val albumName: String,
    val albumThumbnailUri: Uri,
    val albumLastModified: Long,
    val albumMediaType: MediaType,
    var albumMediaCount: Int
): Parcelable