package com.jawahir.mediagallery.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.ItemAlbumBinding
import com.jawahir.mediagallery.ui.uimodels.MediaModels

class AlbumViewHolder(
    private val binding: ItemAlbumBinding,
    private val onItemClick: (MediaModels) -> Unit
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
            executePendingBindings()
        }
    }

    init {
        binding.root.setOnClickListener {
            getItem()?.let(onItemClick)
        }
    }

    private fun getItem(): MediaModels? {
        val adapter = bindingAdapter as? AlbumAdapter // Get the adapter, if it's the right type
        val list = adapter?.currentList // Get the list of items from the adapter, if it exists
        val position = bindingAdapterPosition // Get the position of this item

        return list?.getOrNull(position) // Return the item at the position, or null if it's out of bounds
    }
}