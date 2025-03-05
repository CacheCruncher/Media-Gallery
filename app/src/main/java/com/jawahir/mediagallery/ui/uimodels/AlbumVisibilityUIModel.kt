package com.jawahir.mediagallery.ui.uimodels

import com.jawahir.mediagallery.data.MediaResult


data class AlbumVisibilityUIModel<T>(private val state: MediaResult<T>) : AlbumUIVisibility {
    override fun isProgressBarVisible() = state is MediaResult.Loading
    override fun isItemVisible() = state is MediaResult.Success
    override fun isErrorVisible() = state is MediaResult.Error
}