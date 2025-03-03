package com.jawahir.mediagallery.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel

class AlbumDetailFragment: Fragment(R.layout.fragment_album_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val albumUIModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("albumUiModel", AlbumUIModel::class.java)
        } else {
            arguments?.getParcelable<AlbumUIModel>("albumUiModel")
        }
        albumUIModel?.let {

        }
    }
}