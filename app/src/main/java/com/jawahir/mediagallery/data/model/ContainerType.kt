package com.jawahir.mediagallery.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MediaType:Parcelable{
    VIDEO,
    IMAGE
}