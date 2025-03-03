package com.jawahir.mediagallery.ui.uimodels

import android.net.Uri
import android.os.Parcelable
import com.jawahir.multimediagallery.database.models.AlbumModel
import com.jawahir.multimediagallery.database.models.MediaType
import kotlinx.parcelize.Parcelize

interface MediaModels {
    fun getId(): Long
    fun getUri(): Uri
    fun getName(): String
    fun getMediaType(): MediaType
    fun isAlbum(): Boolean
    fun getMediaCount(): Int
}

@Parcelize
data class AlbumUIModel(
    private val albumModel: AlbumModel
) : MediaModels, Parcelable {
    override fun getId() = albumModel.albumId

    override fun getUri() = albumModel.albumThumbnailUri

    override fun getName() = albumModel.albumName

    override fun getMediaType() = albumModel.albumMediaType

    override fun isAlbum() = true

    override fun getMediaCount() = albumModel.albumMediaCount
}