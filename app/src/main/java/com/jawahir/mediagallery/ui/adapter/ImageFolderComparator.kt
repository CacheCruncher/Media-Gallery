package com.jawahir.mediagallery.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jawahir.mediagallery.ui.uimodels.MediaModels

class ImageFolderComparator : DiffUtil.ItemCallback<MediaModels>() {

    override fun areItemsTheSame(oldItem: MediaModels, newItem: MediaModels) =
        oldItem.getId() == newItem.getId()

    override fun areContentsTheSame(oldItem: MediaModels, newItem: MediaModels) =
        oldItem.getUri() == newItem.getUri()
}