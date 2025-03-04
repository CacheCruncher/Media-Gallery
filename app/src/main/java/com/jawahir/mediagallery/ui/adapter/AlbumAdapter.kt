package com.jawahir.mediagallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jawahir.mediagallery.databinding.ItemAlbumBinding
import com.jawahir.mediagallery.ui.uimodels.MediaModels

class AlbumAdapter(
    private val onItemClick: (MediaModels) -> Unit
) : ListAdapter<MediaModels, AlbumViewHolder>(ImageFolderComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding, onItemClick = { position ->
            getItem(position)?.let {
                onItemClick(it)
            }
        })
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}


