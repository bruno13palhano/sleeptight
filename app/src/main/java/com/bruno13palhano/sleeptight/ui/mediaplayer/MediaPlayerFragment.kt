package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.media3.common.*
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentMediaPlayerBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

@UnstableApi class MediaPlayerFragment : Fragment() {
    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private val browser: MediaBrowser?
        get() = if (browserFuture.isDone) browserFuture.get() else null

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    private lateinit var mediaListAdapter: PlayingMediaItemArrayAdapter
    private val subItemMediaLst: MutableList<MediaItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_media_player, container, false)
        val view = binding.root

        mediaListAdapter = PlayingMediaItemArrayAdapter(requireActivity(), R.layout.fragment_media_player, subItemMediaLst)
        binding.currentPlayingList.adapter = mediaListAdapter
        binding.currentPlayingList.setOnItemClickListener { adapterView, view, position, l ->
            val controller = this.controller ?: return@setOnItemClickListener
            controller.seekToDefaultPosition(position)
            mediaListAdapter.notifyDataSetChanged()

        }

        return view
    }

    override fun onStart() {
        super.onStart()
        initializeController()
        initializerBrowser()
    }

    override fun onResume() {
        super.onResume()
        binding.playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.playerView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player = null
        releaseController()
        releaseBrowser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializerBrowser() {
        browserFuture =
            MediaBrowser.Builder(
                requireActivity(),
                SessionToken(requireActivity(), ComponentName(requireActivity(), PlaybackService::class.java))
            )
                .buildAsync()
    }

    private fun releaseBrowser() {
        MediaBrowser.releaseFuture(browserFuture)
    }

    private fun initializeController() {
        controllerFuture = MediaController.Builder(
            requireActivity(),
                SessionToken(requireActivity(), ComponentName(requireActivity(), PlaybackService::class.java))
            )
            .buildAsync()
        controllerFuture.addListener({ setController() }, MoreExecutors.directExecutor())
    }

    private fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }

    private fun setController() {
        val controller = this.controller ?: return

        binding.playerView.player = controller

        updateCurrentPlaylistUI()
        updateMediaMetadataUI(controller.mediaMetadata)

        controller.addListener(
            object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    updateMediaMetadataUI(mediaItem?.mediaMetadata ?: MediaMetadata.EMPTY)
                }
            }
        )
    }

    private fun updateMediaMetadataUI(mediaMetadata: MediaMetadata) {
        val title: CharSequence = mediaMetadata.title ?: "No media in the play list"
        mediaListAdapter.notifyDataSetChanged()
    }

    private fun updateCurrentPlaylistUI() {
        val controller = this.controller ?: return
        subItemMediaLst.clear()
        for (i in 0 until controller.mediaItemCount) {
            subItemMediaLst.add(controller.getMediaItemAt(i))
        }

        mediaListAdapter.notifyDataSetChanged()
    }

    private inner class PlayingMediaItemArrayAdapter(
        context: Context,
        viewID: Int,
        mediaItemList: List<MediaItem>
    ) : ArrayAdapter<MediaItem>(context, viewID, mediaItemList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val mediaItem = getItem(position)!!
            val returnConvertView =
                convertView ?: LayoutInflater.from(context).inflate(R.layout.playlist_items, parent, false)

            returnConvertView.findViewById<TextView>(R.id.media_item).text = mediaItem.mediaMetadata.title

            val primaryColorTyped = TypedValue()
            requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary,
                primaryColorTyped, true)

            val secondaryColorTyped = TypedValue()
            requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary,
                secondaryColorTyped, true)

            if (position == controller?.currentMediaItemIndex) {
                returnConvertView.setBackgroundColor(primaryColorTyped.data)
                returnConvertView
                    .findViewById<TextView>(R.id.media_item)
            } else {
                returnConvertView.setBackgroundColor(secondaryColorTyped.data)
                returnConvertView
                    .findViewById<TextView>(R.id.media_item)
            }

            return returnConvertView
        }
    }
}