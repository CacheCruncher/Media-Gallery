package com.jawahir.mediagallery.ui.feature.videoplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.FragmentVideoPlayerBinding
class VideoPlayerFragment: Fragment(R.layout.fragment_video_player) {
    private val args: VideoPlayerFragmentArgs by navArgs()
    private lateinit var binding: FragmentVideoPlayerBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVideoPlayerBinding.bind(view)

        binding.apply {
            uiModel = args.albumDetailUIModel
            videoView.setVideoURI(args.albumDetailUIModel.getUri())
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.setOnPreparedListener{ player ->
                    player.start()
                }
            }

            playButton.setOnClickListener {
                videoStart()
            }

            pauseButton.setOnClickListener{
                videoPause()
            }
        }
    }

    private fun FragmentVideoPlayerBinding.videoPause() {
        if (videoView.isPlaying) {
            videoView.pause()
        }
    }

    private fun FragmentVideoPlayerBinding.videoStart() {
        if (!videoView.isPlaying) {
            videoView.start()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.videoStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoView.stopPlayback()
    }
}

