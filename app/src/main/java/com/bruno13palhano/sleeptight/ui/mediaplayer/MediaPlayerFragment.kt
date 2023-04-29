package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.content.ComponentName
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.util.RepeatModeUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.UriUtil
import androidx.media3.common.util.Util
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentMediaPlayerBinding

@UnstableApi class MediaPlayerFragment : Fragment() {
    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L


    private lateinit var mediaBrowser: MediaBrowserCompat

    private val controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onSessionDestroyed() {
            mediaBrowser.disconnect()
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {

        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {

        }
    }

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(
                    requireActivity(),
                    token
                )
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
            }

            buildTransportControls()
        }

        override fun onConnectionSuspended() {

        }

        override fun onConnectionFailed() {

        }
    }

    fun buildTransportControls() {
        val mediaController = MediaControllerCompat.getMediaController(requireActivity())

        binding.videoView.setOnClickListener {
            val pbState = mediaController.playbackState.state
            if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.transportControls.pause()
            } else {
                mediaController.transportControls.play()
            }
        }

        mediaController.registerCallback(controllerCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_media_player, container, false)
        val view = binding.root

        mediaBrowser = MediaBrowserCompat(
            requireActivity(),
            ComponentName(requireActivity(), MediaPlaybackService::class.java),
            connectionCallback,
            null
        )

        return view
    }

    private fun initializePlayer() {

        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                val mediaItem1 = MediaItem.Builder()
                    .setUri(RawResourceDataSource.buildRawResourceUri(R.raw.bloqueado))
                    .setTag("name")
                    .build()
                exoPlayer.setMediaItem(mediaItem1)
                val mediaItem2 = MediaItem.fromUri(
                    RawResourceDataSource.buildRawResourceUri(R.raw.ficha_limpa))
                exoPlayer.addMediaItem(mediaItem2)
                val mediaItem3 = MediaItem.fromUri(
                    RawResourceDataSource.buildRawResourceUri(R.raw.nao_me_arranha))
                exoPlayer.addMediaItem(mediaItem3)
                val mediaItem4 = MediaItem.fromUri(
                    RawResourceDataSource.buildRawResourceUri(R.raw.quebrando_protocolo))
                exoPlayer.addMediaItem(mediaItem4)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
//            initializePlayer()
        }
        mediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer()
        }
        requireActivity().volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
        MediaControllerCompat.getMediaController(requireActivity())?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }
}