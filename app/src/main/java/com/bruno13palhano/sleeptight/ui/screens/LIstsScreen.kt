package com.bruno13palhano.sleeptight.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.navigation.ListsDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    onItemClick: (route: String) -> Unit
) {
    val items = listOf(
        ListsItem.BabyStatusList,
        ListsItem.NapsList,
        ListsItem.NotificationsList
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.lists_label)) })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach { listsItem ->
                ListsCard(listsItem = listsItem) {
                    onItemClick(listsItem.route)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ListsScreenPreview() {
    val items = listOf(
        ListsItem.BabyStatusList,
        ListsItem.NapsList,
        ListsItem.NotificationsList
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.lists_label)) })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach { listsItem ->
                ListsCard(listsItem = listsItem) {

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsCard(
    listsItem: ListsItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = stringResource(id = listsItem.text),
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(64.dp),
            imageVector = listsItem.imageVector,
            contentDescription = stringResource(id = listsItem.text)
        )
    }
}

sealed class ListsItem(@StringRes val text: Int, val imageVector: ImageVector, val route: String) {
    object BabyStatusList: ListsItem(R.string.all_baby_status_label, Icons.Filled.Image, ListsDestinations.BABY_STATUS_LIST_ROUTE)
    object NapsList: ListsItem(R.string.naps_label, Icons.Filled.Image, ListsDestinations.NAP_LIST_ROUTE)
    object NotificationsList: ListsItem(R.string.notifications_label, Icons.Filled.Image, ListsDestinations.NOTIFICATIONS_LIST_ROUTE)
}