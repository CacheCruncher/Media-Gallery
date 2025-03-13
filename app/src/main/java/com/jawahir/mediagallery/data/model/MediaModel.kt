package com.jawahir.mediagallery.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaModel(
    val id:Long,
    val mediaTitle: String,
    val mediaName: String,
    val mediaLastModified: Long,
    val mediaUri: Uri,
    val mediaType: MediaType
):Parcelable