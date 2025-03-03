package com.jawahir.mediagallery.data.model

import android.net.Uri

data class MediaModel(
    val mediaTitle: String,
    val mediaName: String,
    val mediaLastModified: Long,
    val mediaUri: Uri,
    val mediaThumbnailUri: Uri? = null,
    val mediaType: MediaType
)