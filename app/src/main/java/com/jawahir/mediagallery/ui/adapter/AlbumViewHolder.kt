package com.jawahir.mediagallery.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.ItemAlbumBinding
import com.jawahir.mediagallery.ui.uimodels.MediaModels

class AlbumViewHolder(
    private val binding: ItemAlbumBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(album: MediaModels) {
        binding.apply {
            Glide.with(itemView)
                .load(album.getUri())
                .centerCrop()
                .placeholder(R.drawable.ic_media_placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mediaThumbnailIv)

            model = album
        }
    }

    init {
        binding.apply {
            root.setOnClickListener {
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
        }
    }
}