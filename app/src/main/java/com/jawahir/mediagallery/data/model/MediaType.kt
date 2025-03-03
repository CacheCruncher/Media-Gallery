package com.jawahir.multimediagallery.database.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MediaType:Parcelable{
    VIDEO,
    IMAGE
}