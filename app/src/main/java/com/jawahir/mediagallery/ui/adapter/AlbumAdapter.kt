package com.jawahir.mediagallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.ItemAlbumBinding
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel

class AlbumAdapter(
    private val onItemClick: (AlbumUIModel) -> Unit
) : ListAdapter<AlbumUIModel, AlbumViewHolder>(ImageFolderComparator()) {
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


class AlbumViewHolder(
    private val binding: ItemAlbumBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(album: AlbumUIModel) {
        binding.apply {
            Glide.with(itemView)
                .load(album.albumThumbnailUri)
                .centerCrop()
                .error(R.drawable.ic_media_placeholder_image)
                .into(mediaThumbnailIv)

            displayNameTv.text = album.albumName
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

class ImageFolderComparator : DiffUtil.ItemCallback<AlbumUIModel>() {

    override fun areItemsTheSame(oldItem: AlbumUIModel, newItem: AlbumUIModel) =
        oldItem.albumId == newItem.albumId

    override fun areContentsTheSame(oldItem: AlbumUIModel, newItem: AlbumUIModel) =
        oldItem == newItem
}
