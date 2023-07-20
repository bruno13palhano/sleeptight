package com.bruno13palhano.sleeptight.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.navigation.ListsDestinations
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@Composable
fun ListsScreen(
    onItemClick: (route: String) -> Unit
) {
    val items = listOf(
        ListsItem.BabyStatusList,
        ListsItem.NapsList,
        ListsItem.NotificationsList
    )

    ListContent(
        items = items,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    items: List<ListsItem>,
    onItemClick: (route: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.lists_label)) })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach { listsItem ->
                ListsCard(
                    listsItem = listsItem,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .weight(1F, true)
                ) {
                    onItemClick(listsItem.route)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListsScreenPreview() {
    val items = listOf(
        ListsItem.BabyStatusList,
        ListsItem.NapsList,
        ListsItem.NotificationsList
    )

    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ListContent(
                items = items,
                onItemClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsCard(
    listsItem: ListsItem,
    modifier: Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(128.dp),
                imageVector = listsItem.imageVector,
                contentDescription = stringResource(id = listsItem.text)
            )

            Text(
                text = stringResource(id = listsItem.text),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

sealed class ListsItem(@StringRes val text: Int, val imageVector: ImageVector, val route: String) {
    object BabyStatusList: ListsItem(R.string.all_baby_status_label, Icons.Filled.InsertChartOutlined, ListsDestinations.BABY_STATUS_LIST_ROUTE)
    object NapsList: ListsItem(R.string.naps_label, Icons.Filled.Snooze, ListsDestinations.NAP_LIST_ROUTE)
    object NotificationsList: ListsItem(R.string.notifications_label, Icons.Filled.Notifications, ListsDestinations.NOTIFICATIONS_LIST_ROUTE)
}