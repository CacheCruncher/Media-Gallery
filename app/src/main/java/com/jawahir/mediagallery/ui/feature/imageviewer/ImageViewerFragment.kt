package com.jawahir.mediagallery.ui.feature.imageviewer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.FragmentImageViewerBinding

class ImageViewerFragment : Fragment(R.layout.fragment_image_viewer) {

    private val navArgs: ImageViewerFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentImageViewerBinding.bind(view)
        binding.apply {
            uiModel = navArgs.albumDetailUIModel
            Glide.with(view)
                .load(navArgs.albumDetailUIModel.getUri())
                .fitCenter()
                .placeholder(R.drawable.ic_media_placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mediaIV)
        }
    }
}