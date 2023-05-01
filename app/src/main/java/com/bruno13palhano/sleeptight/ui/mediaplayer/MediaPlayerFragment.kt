package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.*
import androidx.media3.common.C.TRACK_TYPE_TEXT
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.SessionToken
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentMediaPlayerBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    companion object {
        private const val MEDIA_ITEM_ID_KEY = "MEDIA_ITEM_ID_KEY"
        fun createIntent(context: Context, mediaItemID: String): Intent {
            val intent = Intent(context, MediaPlayerFragment::class.java)
            intent.putExtra(MEDIA_ITEM_ID_KEY, mediaItemID)
            return intent
        }
    }

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

        binding.shuffleSwitch.setOnClickListener {
            val controller = this.controller ?: return@setOnClickListener
            controller.shuffleModeEnabled = !controller.shuffleModeEnabled
        }

        binding.repeatSwitch.setOnClickListener {
            val controller = this.controller ?: return@setOnClickListener
            when (controller.repeatMode) {
                Player.REPEAT_MODE_ALL -> controller.repeatMode = Player.REPEAT_MODE_OFF
                Player.REPEAT_MODE_OFF -> controller.repeatMode = Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_ONE -> controller.repeatMode = Player.REPEAT_MODE_ALL
            }
        }

        binding.playerView.setOnClickListener {
            val browser = this.browser ?: return@setOnClickListener
            browser.setMediaItems(subItemMediaLst)
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
        browserFuture.addListener({ displayFolder() }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun releaseBrowser() {
        MediaBrowser.releaseFuture(browserFuture)
    }

    private fun displayFolder() {
        val browser = this.browser ?: return
//        val id: String = requireActivity().intent.getStringExtra(MEDIA_ITEM_ID_KEY)!!

        val li = browser.getChildren("[artist]Gusttavo Lima", 0, Int.MAX_VALUE, null)
        li.addListener({
//            val result = li.get()!!
//            result.value?.forEach {
//                subItemMediaLst.add(it)
//                println("mediaItem: ${it.mediaId}")
//            }
//            browser.setMediaItems(subItemMediaLst)
//            binding.playerView.player?.setMediaItems(subItemMediaLst)

        }, ContextCompat.getMainExecutor(requireActivity()))

        val mediaItemFuture = browser.getItem("[item]gusttavo_lima_01")
        val childrenFuture = browser.getChildren("[genreID]",0, Int.MAX_VALUE,null)

        mediaItemFuture.addListener(
            {
                val result = mediaItemFuture.get()!!
                println("reuslt aquiuiuasiduif: ${result.value?.mediaMetadata?.title}")
//                result.value?.let {
//                    subItemMediaLst.add(0, it)
//                    browser.setMediaItem(it)
//                    println("não toca ")
//                }
            }, ContextCompat.getMainExecutor(requireActivity())
        )

        childrenFuture.addListener(
            {
                val result = childrenFuture.get()
                val children = result.value

//                subItemMediaLst.clear()
//                subItemMediaLst.addAll(children!!)
            }, ContextCompat.getMainExecutor(requireActivity())
        )
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
        updateShuffleSwitchUI(controller.shuffleModeEnabled)
        updateRepeatSwitchUI(controller.repeatMode)
        binding.playerView.setShowSubtitleButton(controller.currentTracks.isTypeSupported(
            TRACK_TYPE_TEXT))

        controller.addListener(
            object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    updateMediaMetadataUI(mediaItem?.mediaMetadata ?: MediaMetadata.EMPTY)
                }

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    updateShuffleSwitchUI(shuffleModeEnabled)
                }

                override fun onRepeatModeChanged(repeatMode: Int) {
                    updateRepeatSwitchUI(repeatMode)
                }

                override fun onTracksChanged(tracks: Tracks) {
                    binding.playerView.setShowSubtitleButton(tracks.isTypeSelected(TRACK_TYPE_TEXT))
                }
            }
        )
    }

    private fun updateShuffleSwitchUI(shuffleModeModeEnabled: Boolean) {
        val resId =
            if (shuffleModeModeEnabled) R.drawable.baseline_shuffle_on_24
            else R.drawable.baseline_shuffle_24
        binding.shuffleSwitch.setImageDrawable(ContextCompat.getDrawable(requireActivity(), resId))
    }

    private fun updateRepeatSwitchUI(repeatMode: Int) {
        val resId: Int =
            when (repeatMode) {
                Player.REPEAT_MODE_OFF -> R.drawable.baseline_repeat_one_24
                Player.REPEAT_MODE_ONE -> R.drawable.baseline_repeat_one_on_24
                Player.REPEAT_MODE_ALL -> R.drawable.baseline_repeat_24
                else -> R.drawable.baseline_repeat_one_24
            }
        binding.repeatSwitch.setImageDrawable(ContextCompat.getDrawable(requireActivity(), resId))
    }

    private fun updateMediaMetadataUI(mediaMetadata: MediaMetadata) {
        val title: CharSequence = mediaMetadata.title ?: "No media in the play list"

        binding.videoTitle.text = title
        binding.videoAlbum.text = mediaMetadata.albumTitle
        binding.videoArtist.text = mediaMetadata.artist
        binding.videoGenre.text = mediaMetadata.genre

        mediaListAdapter.notifyDataSetChanged()
    }

    private fun updateCurrentPlaylistUI() {
        val controller = this.controller ?: return
        subItemMediaLst.clear()
        for (i in 0 until controller.mediaItemCount) {
            println("aqui")
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

            if (position == controller?.currentMediaItemIndex) {
                returnConvertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                returnConvertView
                    .findViewById<TextView>(R.id.media_item)
                    .setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                returnConvertView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                returnConvertView
                    .findViewById<TextView>(R.id.media_item)
                    .setTextColor(ContextCompat.getColor(context, R.color.white))
            }

            return returnConvertView
        }
    }
}