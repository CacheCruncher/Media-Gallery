package com.jawahir.mediagallery.ui.uimodels

import android.net.Uri
import android.os.Parcelable
import com.jawahir.mediagallery.data.model.AlbumModel
import kotlinx.parcelize.Parcelize

interface MediaModels {
    fun getId(): Long
    fun getUri(): Uri
    fun getName(): String
    fun getContainerType(): ContainerUIModel
    fun getMediaCount(): String
}

@Parcelize
data class AlbumUIModel(
    private val albumModel: AlbumModel
) : MediaModels, Parcelable {
    override fun getId() = albumModel.albumId

    override fun getUri() = albumModel.albumThumbnailUri

    override fun getName() = albumModel.albumName

    override fun getContainerType() = ContainerUIModel.ALBUM

    override fun getMediaCount() = "Items : ${albumModel.albumMediaCount}"
}