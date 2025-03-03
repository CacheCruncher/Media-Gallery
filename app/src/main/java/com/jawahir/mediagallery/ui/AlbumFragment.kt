package com.jawahir.mediagallery.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.databinding.FragmentAlbumBinding
import com.jawahir.mediagallery.ui.adapter.AlbumAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {

    private val viewModel: AlbumViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumBinding.bind(view)
        val albumAdapter = AlbumAdapter { albumUIModel ->
            val bundle = Bundle().apply {
                putParcelable("albumUIModel", albumUIModel)
            }
            findNavController().navigate(R.id.action_albumFragment_to_albumDetailFragment,bundle)
        }

        binding.apply {
            albumListRv.apply {
                adapter = albumAdapter
                layoutManager = GridLayoutManager(context, 3)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { mediaResult ->
                    val result = mediaResult?:return@collectLatest
                    when(result){
                        is MediaResult.Loading -> {
                            binding.progressbar.isVisible = true
                            binding.albumListRv.isVisible = false
                        }
                        is MediaResult.Success -> {
                            binding.progressbar.isVisible = false
                            binding.albumListRv.isVisible = true
                            result.data?.let {
                                albumAdapter.submitList(it)
                            }
                        }
                        is MediaResult.Error -> {

                        }
                    }
                }
            }
        }
    }
}