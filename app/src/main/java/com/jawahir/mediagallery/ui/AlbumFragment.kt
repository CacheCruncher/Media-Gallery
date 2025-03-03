package com.jawahir.mediagallery.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.FragmentAlbumBinding
import com.jawahir.mediagallery.ui.adapter.AlbumAdapter
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import com.jawahir.multimediagallery.database.models.MediaType

class AlbumFragment: Fragment(R.layout.fragment_album) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumBinding.bind(view)
        val adapter2 = AlbumAdapter{ albumUIModel ->
            // navigate to fragment detail with the album id
            Toast.makeText(context,"${albumUIModel.albumName} album name", Toast.LENGTH_LONG).show()
        }
        binding.albumListRv.apply {
            adapter = adapter2

            layoutManager = GridLayoutManager(context,3)

        }
        adapter2.submitList(fakedata())
        binding.albumListRv.isVisible = true
    }


    fun fakedata():List<AlbumUIModel> = listOf(
        AlbumUIModel(albumId =-1829889111, albumName = "WhatsApp Images", albumThumbnailUri = Uri.parse("content://media/external/images/media/1000178970"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
        AlbumUIModel(albumId =318896427, albumName = "WhatsApp Images", albumThumbnailUri = Uri.parse("content:////media/external/images/media/1000178904"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
        AlbumUIModel(albumId =-1313584517, albumName = "Screenshots", albumThumbnailUri = Uri.parse("media/external/images/media/1000178374"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
        AlbumUIModel(albumId =-1739773001, albumName = "Camera", albumThumbnailUri = Uri.parse("content://media/external/images/media/1000177664"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
        AlbumUIModel(albumId =-1739773001, albumName = "Phone", albumThumbnailUri = Uri.parse("content://media/external/images/media/1000177664"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
        AlbumUIModel(albumId =-1739773001, albumName = "Eid", albumThumbnailUri = Uri.parse("content://media/external/images/media/1000177664"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
        AlbumUIModel(albumId =-1739773001, albumName = "December", albumThumbnailUri = Uri.parse("content://media/external/images/media/1000177664"), albumMediaType = MediaType.IMAGE, albumMediaCount = 234),
    )
}