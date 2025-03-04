package com.jawahir.mediagallery.ui.feature.imageviewer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.FragmentImageViewerBinding

class ImageViewerFragment : Fragment(R.layout.fragment_image_viewer) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentImageViewerBinding.bind(view)

        val args = ImageViewerFragmentArgs.fromBundle(requireArguments())
        val albumDetailUIModel = args.albumDetailUIModel

        binding.apply {
            uiModel = albumDetailUIModel
            Glide.with(view)
                .load(albumDetailUIModel.getUri())
                .fitCenter()
                .error(R.drawable.ic_media_placeholder_image)
                .into(mediaIV)
        }
    }
}