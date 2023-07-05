package com.bruno13palhano.sleeptight.ui.screens

import android.content.ComponentName
import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.mediaplayer.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

private lateinit var controllerFuture: ListenableFuture<MediaController>
private val controller: MediaController?
    get() = if (controllerFuture.isDone) controllerFuture.get() else null
private val subItemMediaLst: MutableList<MediaItem> = mutableListOf()
private var mediaI: MediaItem? = null

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun PlayerScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                initializeController(context) {
                    isVisible = true
                }
            } else if (event == Lifecycle.Event.ON_STOP) {
                releaseController()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (isVisible) {
        PlayerContent(context = context)
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun initializeController(context: Context, launched: () -> Unit) {
    controllerFuture = MediaController.Builder(
        context,
        SessionToken(context, ComponentName(context, PlaybackService::class.java))
    )
        .buildAsync()
    controllerFuture.addListener({ setController(launched) }, MoreExecutors.directExecutor())
}

private fun releaseController() {
    MediaController.releaseFuture(controllerFuture)
}

private fun setController(launched: () -> Unit) {
    val ctrl = controller ?: return

    launched()
    updateCurrentPlaylistUI()

    ctrl.addListener(
        object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                mediaI = mediaItem
            }
        }
    )
}

private fun updateCurrentPlaylistUI() {
    val ctrl = controller ?: return
    subItemMediaLst.clear()
    for (i in 0 until ctrl.mediaItemCount) {
        subItemMediaLst.add(ctrl.getMediaItemAt(i))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun PlayerContent(
    context: Context,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.player_label)) })
        }
    ) {
        var isPlaying by remember { mutableStateOf(false) }
        var currentMusicIndex by remember { mutableIntStateOf(0) }
        var title by remember { mutableStateOf("") }
        var artist by remember { mutableStateOf("") }
        var album by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(it)
        ) {
            val playerView = playerView(context = context)
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxHeight = 300.dp),
                factory = { playerView },
                update = { player ->
                    player.player?.addListener(object : Player.Listener {
                        override fun onEvents(player: Player, events: Player.Events) {
                            currentMusicIndex = player.currentMediaItemIndex
                            title = player.mediaMetadata.title.toString()
                            artist = player.mediaMetadata.artist.toString()
                            album = player.mediaMetadata.albumTitle.toString()
                            isPlaying = player.isPlaying
                        }
                    })
                }
            )

            CurrentMediaCard(
                title = title,
                artist = artist,
                album = album
            )

            LazyColumn(
                modifier = Modifier
                    .padding(top = 4.dp),
            ) {
                itemsIndexed(items = subItemMediaLst) { index, mediaItem ->
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
                        }
                    )
                }
            }
        }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun playerView(context: Context): PlayerView {
    val playerView = PlayerView(context).apply {
        showController()
        useController = true
        player = controller
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
    PlayerContent(
        context = LocalContext.current
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicItemList(
    index: Int,
    isPlaying: Boolean,
    isCurrentMusic: Boolean,
    title: String,
    artist: String,
    onItemClick: (index: Int) -> Unit,
    onPlayPauseClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        onClick = { onItemClick(index) }
    ) {
        Box(
            modifier = if (isCurrentMusic) {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
            } else {
                Modifier.fillMaxWidth()
            }
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                    text = artist,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (isCurrentMusic) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    onClick = onPlayPauseClick
                ) {
                    if (isPlaying) {
                        Icon(
                            imageVector = Icons.Filled.Pause,
                            contentDescription = stringResource(id = R.string.player_label)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = stringResource(id = R.string.player_label)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentMediaCard(
    title: String,
    artist: String,
    album: String,
) {
    Column {
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            text = "$artist, $album",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}