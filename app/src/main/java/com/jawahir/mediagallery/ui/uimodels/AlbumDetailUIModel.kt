package com.jawahir.mediagallery.ui.uimodels

data class AlbumDetailUIModel(
    private val mediaModels: MediaModels
) : MediaModels {
    override fun getId() = mediaModels.getId()

    override fun getUri() = mediaModels.getUri()

    override fun getName() = mediaModels.getName()

    override fun getMediaType() = mediaModels.getMediaType()

    override fun isAlbum() = false

    override fun getMediaCount() = 1
}

