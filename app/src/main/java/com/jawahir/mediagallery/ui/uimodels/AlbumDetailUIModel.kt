package com.jawahir.mediagallery.ui.uimodels

import com.jawahir.mediagallery.data.model.MediaModel
import com.jawahir.mediagallery.data.model.MediaType

data class AlbumDetailUIModel(
    private val mediaModels: MediaModel
) : MediaModels {
    override fun getId() = mediaModels.mediaLastModified

    override fun getUri() = mediaModels.mediaUri

    override fun getName() = mediaModels.mediaName

    override fun getContainerType() =  when (mediaModels.mediaType) {
        MediaType.IMAGE -> ContainerUIModel.IMAGE
        MediaType.VIDEO -> ContainerUIModel.VIDEO
    }

    override fun getMediaCount() = "1"
}

