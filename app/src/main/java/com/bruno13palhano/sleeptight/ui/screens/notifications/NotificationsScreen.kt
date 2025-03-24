package com.bruno13palhano.sleeptight.ui.screens.notifications

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.model.Notification
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.ItemList
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil

@Composable
fun NotificationsScreen(
    onItemClick: (notificationId: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
) {
    val notificationList by notificationsViewModel.allNotifications.collectAsStateWithLifecycle()

    NotificationsContent(
        notificationList = notificationList,
        onItemClick = onItemClick,
        onNavigationIconClick = onNavigationIconClick,
        onAddButtonClick = onAddButtonClick,
        onDeleteItemClick = { notificationId ->
            notificationsViewModel.deleteNotification(notificationId) {
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    notificationList: List<Notification>,
    onItemClick: (id: Long) -> Unit,
    onDeleteItemClick: (id: Long) -> Unit,
    onNavigationIconClick: () -> Unit,
    onAddButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.notifications_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_button),
                )
            }
        },
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(items = notificationList, key = { notification ->
                notification.id
            }) { notification ->
                ItemList(
                    id = notification.id,
                    title = notification.title,
                    date = DateFormatUtil.format(notification.date),
                    onItemClick = { onItemClick(notification.id) },
                    onDeleteItemClick = { onDeleteItemClick(notification.id) },
                )
            }
        }
    }
}

@Preview
@Composable
fun NotificationsScreenPreview() {
    val notificationList = mutableListOf<Notification>()
    for (i in 1..15) {
        notificationList.add(
            Notification(
                id = i.toLong(),
                title = "${stringResource(id = R.string.notification_label)} $i",
                description = "",
                time = 0L,
                date = 0L,
                repeat = false,
            ),
        )
    }

    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NotificationsContent(
                notificationList = notificationList,
                onItemClick = {},
                onNavigationIconClick = {},
                onAddButtonClick = {},
                onDeleteItemClick = {},
            )
        }
    }
}
