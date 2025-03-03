package com.jawahir.mediagallery.ui.uimodels

import android.net.Uri
import com.jawahir.multimediagallery.database.models.MediaType

data class AlbumDetailUIModel(
    val mediaId:Long,
    val mediaTitle: String,
    val mediaUri: Uri,
    val mediaType: MediaType
):MediaModels{
    override fun getId() = mediaId

    override fun getUri() = mediaUri

    override fun getName() = mediaTitle
}

