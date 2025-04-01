package com.bruno13palhano.sleeptight.ui.screens

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.ui.PlayerView
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.mediaplayer.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private var controllerFuture: ListenableFuture<MediaController>? = null
private var playbackService by mutableStateOf<PlaybackService.PlaybackBinder?>(null)
private var isBound = false

private val serviceConnection = object : ServiceConnection {
    @UnstableApi
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        playbackService = service as PlaybackService.PlaybackBinder
        isBound = true
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        playbackService = null
        isBound = false
    }
}

@Composable
fun PlayerScreen() {
    PlayerContent()
}

private fun releaseController(context: Context) {
    if (isBound) {
        context.unbindService(serviceConnection)
        isBound = false
    }

    controllerFuture?.let { MediaController.releaseFuture(it) }
}

@androidx.annotation.OptIn(UnstableApi::class)
private fun startService(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(Intent(context, PlaybackService::class.java))
    }
    context.bindService(
        Intent(context, PlaybackService::class.java),
        serviceConnection,
        BIND_AUTO_CREATE,
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerContent() {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val mainHandler by remember { mutableStateOf(Handler(Looper.getMainLooper())) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.player_label)) })
        },
    ) { paddingValues ->
        var isPlaying by remember { mutableStateOf(false) }
        var currentMusicIndex by remember { mutableIntStateOf(0) }
        val controller = remember { mutableStateOf<MediaController?>(null) }
        val mediaItemList = remember { mutableStateListOf<MediaItem>() }
        var currentMediaItem by remember { mutableStateOf<MediaItem?>(null) }

        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        scope.launch(Dispatchers.IO) {
                            startService(context = context)
                        }
                    }
                    Lifecycle.Event.ON_START -> {
                        scope.launch(Dispatchers.IO) {
                            setController(
                                context = context,
                                controller = controller,
                                onPlaying = { isPlaying = it },
                                onCurrentMediaIndex = { currentMusicIndex = it },
                                onCurrentMedia = { currentMediaItem = it },
                                onMediaItems = {
                                    mainHandler.post {
                                        mediaItemList.clear()
                                        mediaItemList.addAll(it)
                                    }
                                    if (mediaItemList.isNotEmpty() && currentMediaItem == null) {
                                        currentMediaItem = mediaItemList[0]
                                    }
                                },
                            )
                        }
                    }
                    Lifecycle.Event.ON_STOP -> {
                        mainHandler.post {
                            releaseController(context)
                        }
                    }
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        LaunchedEffect(playbackService) {
            if (playbackService != null) {
                setController(
                    context = context,
                    controller = controller,
                    onPlaying = { isPlaying = it },
                    onCurrentMediaIndex = { currentMusicIndex = it },
                    onCurrentMedia = { currentMediaItem = it },
                    onMediaItems = {
                        mainHandler.post {
                            mediaItemList.clear()
                            mediaItemList.addAll(it)
                            if (mediaItemList.isNotEmpty()) {
                                currentMediaItem = mediaItemList[0]
                            }
                        }
                    },
                )
            }
        }

        DisposableEffect(controller.value) {
            val listener = controller.value?.let {
                object : Player.Listener {
                    override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                        isPlaying = isPlayingNow
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        currentMusicIndex = it.currentMediaItemIndex
                        currentMediaItem = mediaItem
                    }
                }.also { listener -> it.addListener(listener) }
            }
            onDispose {
                listener?.let { controller.value?.removeListener(it) }
            }
        }

        val playerView = playerView(context = context)
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
        ) {
            stickyHeader {
                Surface {
                    Column {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sizeIn(maxHeight = 300.dp),
                            factory = { playerView },
                            update = { player ->
                                player.player = controller.value
                                player.showController()
                                player.useController = true
                            },
                        )
                        currentMediaItem?.let {
                            CurrentMediaCard(
                                title = currentMediaItem?.mediaMetadata?.title.toString(),
                                artist = currentMediaItem?.mediaMetadata?.artist.toString(),
                                album = currentMediaItem?.mediaMetadata?.albumTitle.toString(),
                            )
                        }
                    }
                }
            }
            itemsIndexed(
                items = mediaItemList,
                key = { _, item -> item.mediaId },
            ) { index, mediaItem ->
                MusicItemList(
                    index = index,
                    isPlaying = isPlaying,
                    isCurrentMusic = currentMusicIndex == index,
                    title = mediaItem.mediaMetadata.title.toString(),
                    artist = mediaItem.mediaMetadata.artist.toString(),
                    onItemClick = { idx ->
                        playerView.player?.seekToDefaultPosition(idx)
                        currentMusicIndex = playerView.player?.currentMediaItemIndex ?: 0
                    },
                    onPlayPauseClick = {
                        isPlaying = if (isPlaying) {
                            playerView.player?.pause()
                            currentMusicIndex = playerView.player?.currentMediaItemIndex ?: 0
                            false
                        } else {
                            playerView.player?.prepare()
                            playerView.player?.play()
                            currentMusicIndex = playerView.player?.currentMediaItemIndex ?: 0
                            true
                        }
                    },
                )
            }
        }
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

@androidx.annotation.OptIn(UnstableApi::class)
private fun setController(
    context: Context,
    controller: MutableState<MediaController?>,
    onPlaying: (isPlaying: Boolean) -> Unit,
    onCurrentMediaIndex: (index: Int) -> Unit,
    onCurrentMedia: (currentMedia: MediaItem?) -> Unit,
    onMediaItems: (mediaItems: List<MediaItem>) -> Unit,
) {
    startService(context = context)

    playbackService?.getMediaSession()?.let { session ->
        controllerFuture = MediaController.Builder(context, session.token)
            .buildAsync()
        controllerFuture?.addListener(
            {
                try {
                    val mediaController = controllerFuture?.get()
                    controller.value = mediaController

                    val mediaItems = getMediaItems(controller = controller.value)
                    onMediaItems(mediaItems)

                    if (mediaController != null) {
                        onPlaying(mediaController.isPlaying)
                        onCurrentMediaIndex(mediaController.currentMediaItemIndex)

                        mediaController.addListener(
                            object : Player.Listener {
                                override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                                    onPlaying(isPlayingNow)
                                }

                                override fun onMediaItemTransition(
                                    mediaItem: MediaItem?,
                                    reason: Int,
                                ) {
                                    onCurrentMediaIndex(mediaController.currentMediaItemIndex)
                                    onCurrentMedia(mediaItem)
                                }
                            },
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            MoreExecutors.directExecutor(),
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun playerView(context: Context): PlayerView {
    val playerView = PlayerView(context).apply {
        showController()
        useController = true
        player = controllerFuture?.get()
        setRepeatToggleModes(REPEAT_TOGGLE_MODE_ONE or REPEAT_TOGGLE_MODE_ALL)
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    playerView.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    playerView.onPause()
                }
                Lifecycle.Event.ON_STOP -> {
                    playerView.player = null
                }
                else -> {}
            }
        }

        lifecycle.lifecycle.addObserver(observer)

        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }

    return playerView
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    PlayerContent()
}

@Composable
fun MusicItemList(
    index: Int,
    isPlaying: Boolean,
    isCurrentMusic: Boolean,
    title: String,
    artist: String,
    onItemClick: (index: Int) -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 1.dp)
            .border(1.dp, MaterialTheme.colorScheme.inverseOnSurface, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        onClick = { onItemClick(index) },
    ) {
        Box(
            modifier = if (isCurrentMusic) {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            } else {
                Modifier.fillMaxWidth()
            },
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                    text = artist,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            if (isCurrentMusic) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    onClick = onPlayPauseClick,
                ) {
                    if (isPlaying) {
                        Icon(
                            imageVector = Icons.Filled.Pause,
                            contentDescription = stringResource(id = R.string.player_label),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = stringResource(id = R.string.player_label),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentMediaCard(title: String, artist: String, album: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.secondaryContainer, CircleShape),
            painter = painterResource(id = R.drawable.logo_1),
            contentDescription = stringResource(id = R.string.baby_photo_label),
        )
        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                text = "$artist, $album",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
