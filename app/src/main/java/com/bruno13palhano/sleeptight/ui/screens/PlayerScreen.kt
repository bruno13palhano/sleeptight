package com.bruno13palhano.sleeptight.ui.screens

import android.content.ComponentName
import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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


@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun PlayerScreen() {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }

    if (isVisible) {
        PlayerContent(context = context)
    }

    LaunchedEffect(key1 = Unit) {
        initializeController(context) {
            isVisible = true
        }
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
//                super.onMediaItemTransition(mediaItem, reason)
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
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            DisposableEffect(
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(maxHeight = 300.dp),
                    factory = {
                        PlayerView(context).apply {
                            showController()
                            useController = true
                            player = controller
                            setRepeatToggleModes(REPEAT_TOGGLE_MODE_ONE or REPEAT_TOGGLE_MODE_ALL)
                            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        }
                    },
                    update = {}
                )
            ) {
                onDispose { releaseController() }
            }
            LazyColumn {
                items(items = subItemMediaLst) { mediaItem ->
                    Text(text = mediaItem.mediaId)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    PlayerContent(
        context = LocalContext.current
    )
}