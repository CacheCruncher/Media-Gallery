package com.jawahir.mediagallery.ui.feature.albums

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.databinding.FragmentAlbumBinding
import com.jawahir.mediagallery.ui.adapter.AlbumAdapter
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import com.jawahir.mediagallery.ui.uimodels.AlbumVisibilityUIModel
import com.jawahir.mediagallery.ui.uimodels.MediaModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {

    private val viewModel: AlbumViewModel by viewModels()
    private var _binding: FragmentAlbumBinding? = null
    private val binding: FragmentAlbumBinding
        get() = _binding ?: throw IllegalStateException("Binding not initialized")

    private val albumAdapter: AlbumAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AlbumAdapter(::onAlbumItemClicked) // function reference, equivalent of lambda expression
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAlbumBinding.bind(view)
        initializeRecyclerView()
        observeViewModelState()
    }

    private fun initializeRecyclerView() {
        binding.albumListRv.apply {
            layoutManager = layoutManager ?: GridLayoutManager(context, 3)
            adapter = albumAdapter

            //optimization
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest(::handleViewModelState)
            }
        }
    }

    private fun handleViewModelState(mediaResult: MediaResult<List<AlbumUIModel>>) {
        binding.uiModel = AlbumVisibilityUIModel(mediaResult)
        if (mediaResult is MediaResult.Success) {
            mediaResult.data.let(albumAdapter::submitList)
        }
    }

    private fun onAlbumItemClicked(mediaUIModel: MediaModels) {
        if (mediaUIModel is AlbumUIModel) {
            val navDirections =
                AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(mediaUIModel)
            findNavController().navigate(navDirections)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}