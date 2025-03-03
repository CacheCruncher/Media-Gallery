package com.jawahir.mediagallery.ui.uimodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ContainerUIModel :Parcelable{
    IMAGE,
    VIDEO,
    ALBUM
}