package com.jawahir.mediagallery.ui.uimodels

import android.net.Uri
import com.jawahir.multimediagallery.database.models.MediaType

interface MediaModels {
    fun getId(): Long
    fun getUri(): Uri
    fun getName(): String
}


data class AlbumUIModel(
    val albumId: Long,
    val albumName: String,
    val albumThumbnailUri: Uri,
    val albumMediaType: MediaType,
    var albumMediaCount: Int
) : MediaModels {
    override fun getId() = albumId

    override fun getUri() = albumThumbnailUri

    override fun getName() = albumName

}