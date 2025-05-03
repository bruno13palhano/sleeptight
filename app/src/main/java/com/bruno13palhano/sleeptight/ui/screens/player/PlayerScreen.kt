package com.bruno13palhano.sleeptight.ui.screens.player

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.ui.PlayerView
import com.bruno13palhano.sleeptight.R

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(viewModel: PlayerViewModel = hiltViewModel()) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentMusicIndex by viewModel.currentMusicIndex.collectAsState()
    val mediaItemList by viewModel.mediaItemList.collectAsState()
    val currentMediaItem by viewModel.currentMediaItem.collectAsState()
    val controller by viewModel.controller.collectAsState()

    PlayerContent(
        isPlaying = isPlaying,
        currentMusicIndex = currentMusicIndex,
        mediaItemList = mediaItemList,
        currentMediaItem = currentMediaItem,
        controller = controller,
        onStartService = viewModel::startService,
        onSyncController = viewModel::syncController,
        onReleaseController = viewModel::releaseController,
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerContent(
    isPlaying: Boolean,
    currentMusicIndex: Int,
    mediaItemList: List<MediaItem>,
    currentMediaItem: MediaItem?,
    controller: MediaController?,
    onStartService: () -> Unit,
    onSyncController: () -> Unit,
    onReleaseController: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> onStartService

                Lifecycle.Event.ON_RESUME -> onSyncController

                Lifecycle.Event.ON_STOP -> onReleaseController

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) { onSyncController() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.player_label)) })
        },
    ) { paddingValues ->
        val playerView = playerView(context = context, controller = controller)
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            stickyHeader {
                Surface {
                    Column {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sizeIn(maxHeight = 300.dp),
                            factory = { playerView },
                            update = { player ->
                                player.player = controller
                                player.showController()
                                player.useController = true
                            },
                        )
                        currentMediaItem?.let {
                            CurrentMediaCard(
                                title = it.mediaMetadata.title.toString(),
                                artist = it.mediaMetadata.artist.toString(),
                                album = it.mediaMetadata.albumTitle.toString(),
                            )
                        }
                    }
                }
            }
            itemsIndexed(mediaItemList, key = { _, item -> item.mediaId }) { index, mediaItem ->
                MusicItemList(
                    index = index,
                    isPlaying = isPlaying,
                    isCurrentMusic = currentMusicIndex == index,
                    title = mediaItem.mediaMetadata.title.toString(),
                    artist = mediaItem.mediaMetadata.artist.toString(),
                    onItemClick = { idx ->
                        controller?.seekToDefaultPosition(idx)
                    },
                    onPlayPauseClick = {
                        controller?.let { player ->
                            if (isPlaying) {
                                player.pause()
                            } else {
                                player.prepare()
                                player.play()
                            }
                        }
                    },
                )
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun playerView(context: Context, controller: MediaController?): PlayerView {
    val playerView = PlayerView(context).apply {
        showController()
        useController = true
        setRepeatToggleModes(REPEAT_TOGGLE_MODE_ONE or REPEAT_TOGGLE_MODE_ALL)
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    LaunchedEffect(controller) {
        playerView.player = controller
    }

    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> playerView.onResume()
                Lifecycle.Event.ON_PAUSE -> playerView.onPause()
                Lifecycle.Event.ON_STOP -> playerView.player = null
                else -> {}
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose { lifecycle.lifecycle.removeObserver(observer) }
    }

    return playerView
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    PlayerContent(
        isPlaying = false,
        currentMusicIndex = 0,
        mediaItemList = emptyList(),
        currentMediaItem = null,
        controller = null,
        onStartService = {},
        onSyncController = {},
        onReleaseController = {},
    )
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
