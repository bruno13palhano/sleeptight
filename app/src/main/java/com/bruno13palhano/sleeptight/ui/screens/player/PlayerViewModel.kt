package com.bruno13palhano.sleeptight.ui.screens.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.bruno13palhano.sleeptight.ui.mediaplayer.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : ViewModel() {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentMusicIndex = MutableStateFlow(0)
    val currentMusicIndex: StateFlow<Int> = _currentMusicIndex

    private val _mediaItemList = MutableStateFlow<List<MediaItem>>(emptyList())
    val mediaItemList: StateFlow<List<MediaItem>> = _mediaItemList

    private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
    val currentMediaItem: StateFlow<MediaItem?> = _currentMediaItem

    private val _controller = MutableStateFlow<MediaController?>(null)
    val controller: StateFlow<MediaController?> = _controller

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var playbackService: PlaybackService.PlaybackBinder? = null
    private var isBound = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playbackService = service as PlaybackService.PlaybackBinder
            isBound = true
            viewModelScope.launch(Dispatchers.IO) {
                setController()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playbackService = null
            isBound = false
        }
    }

    fun startService() {
        viewModelScope.launch(Dispatchers.IO) {
            val intent = Intent(appContext, PlaybackService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appContext.startForegroundService(intent)
            }

            if (!isBound) {
                appContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    private suspend fun setController() {
        playbackService?.getMediaSession()?.let { session ->
            val mediaControllerFuture = withContext(Dispatchers.IO) {
                MediaController.Builder(appContext, session.token).buildAsync()
            }
            val mediaController = mediaControllerFuture.await()

            withContext(Dispatchers.Main) {
                controllerFuture?.let { MediaController.releaseFuture(it) }
                controllerFuture = mediaControllerFuture
                _controller.value = mediaController
                syncStateWithController(mediaController = mediaController)

                mediaController.addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                            _isPlaying.value = isPlayingNow
                        }

                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            _currentMusicIndex.value = mediaController.currentMediaItemIndex
                            _currentMediaItem.value = mediaItem
                        }
                    },
                )
            }
        }
    }

    private fun syncStateWithController(mediaController: MediaController?) {
        mediaController?.let {
            _isPlaying.value = it.isPlaying
            _currentMusicIndex.value = it.currentMediaItemIndex
            _currentMediaItem.value = it.currentMediaItem
            _mediaItemList.value = getMediaItems(controller = it)
        }
    }

    private fun getMediaItems(controller: MediaController?): List<MediaItem> {
        val result = mutableListOf<MediaItem>()
        controller?.let { ctr ->
            ctr.mediaItemCount.let { items ->
                for (i in 0 until items) {
                    result.add(ctr.getMediaItemAt(i))
                }
            }
        }
        return result
    }

    fun syncController() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isBound && playbackService != null) {
                if (_controller.value == null) {
                    setController()
                } else {
                    withContext(Dispatchers.Main) {
                        _controller.value?.let { mediaController ->
                            syncStateWithController(mediaController = mediaController)
                        }
                    }
                }
            } else {
                startService()
            }
        }
    }

    fun releaseController() {
        viewModelScope.launch {
            mainHandler.post {
                controllerFuture?.let { MediaController.releaseFuture(it) }
                _controller.value = null
                if (isBound) {
                    appContext.unbindService(serviceConnection)
                    isBound = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            mainHandler.post {
                if (isBound) {
                    appContext.unbindService(serviceConnection)
                    isBound = false
                }
            }
        }
    }
}
