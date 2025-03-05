package com.jawahir.mediagallery.ui.uimodels

import android.net.Uri

interface MediaModels {
    fun getId(): Long
    fun getUri(): Uri
    fun getName(): String
    fun getContainerType(): ContainerUIModel
    fun getMediaCount(): String
}