package com.jawahir.mediagallery.ui.feature.albumsDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.databinding.FragmentAlbumDetailBinding
import com.jawahir.mediagallery.ui.adapter.AlbumAdapter
import com.jawahir.mediagallery.ui.uimodels.AlbumDetailUIModel
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import com.jawahir.mediagallery.ui.uimodels.AlbumVisibilityUIModel
import com.jawahir.mediagallery.ui.uimodels.ContainerUIModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    private val viewModel: AlbumDetailViewModel by viewModels()
    private lateinit var uiModel: AlbumUIModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = AlbumDetailFragmentArgs.fromBundle(requireArguments())
        val albumUIModel = args.albumUIModel

        albumUIModel.let {
            uiModel = it
        }

        val binding = FragmentAlbumDetailBinding.bind(view)
        val albumAdapter = AlbumAdapter { uiModel ->
            when(uiModel.getContainerType()){
                ContainerUIModel.IMAGE -> {
                    val action = AlbumDetailFragmentDirections.actionAlbumDetailFragmentToImageViewerFragment(uiModel as AlbumDetailUIModel)
                    findNavController().navigate(action)
                }
                else->{
                    val action = AlbumDetailFragmentDirections.actionAlbumDetailFragmentToVideoPlayerFragment(uiModel as AlbumDetailUIModel)
                    findNavController().navigate(action)
                }
            }

        }

        binding.apply {
            mediaListRv.apply {
                adapter = albumAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { mediaResult ->
                    val result = mediaResult?:return@collectLatest
                    when(result){
                        is MediaResult.Loading -> {
                            binding.uiModel = AlbumVisibilityUIModel(result)
                        }
                        is MediaResult.Success -> {
                            binding.uiModel = AlbumVisibilityUIModel(result)
                            result.data?.let {
                                albumAdapter.submitList(it)
                            }
                        }
                        is MediaResult.Error -> {
                            binding.uiModel = AlbumVisibilityUIModel(result)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(::uiModel.isInitialized)
            viewModel.onStart(uiModel.getId())
    }
}