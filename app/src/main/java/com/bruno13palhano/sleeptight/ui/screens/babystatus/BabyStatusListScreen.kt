 package com.bruno13palhano.sleeptight.ui.screens.babystatus

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
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.ItemList
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil

 @Composable
fun BabyStatusListScreen(
    onItemClick: (babyStatusId: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    babyStatusListViewModel: BabyStatusListViewModel = hiltViewModel()
) {
    val babyStatusList by babyStatusListViewModel.babyStatusList.collectAsStateWithLifecycle()

    BabyStatusContent(
        babyStatusList = babyStatusList,
        onItemClick = onItemClick,
        onNavigationIconClick = onNavigationIconClick,
        onAddButtonClick = onAddButtonClick,
        onDeleteItemClick = { babyStatusId ->
            babyStatusListViewModel.deleteBabyStatus(babyStatusId) {

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyStatusContent(
    babyStatusList: List<BabyStatus>,
    onItemClick: (id: Long) -> Unit,
    onDeleteItemClick: (id: Long) -> Unit,
    onNavigationIconClick: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.all_baby_status_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_button)
                )
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(
                items = babyStatusList,
                key = { babyStatus ->
                    babyStatus.id
                }
            ) { babyStatus ->
                ItemList(
                    id = babyStatus.id,
                    title = babyStatus.title,
                    date = DateFormatUtil.format(babyStatus.date),
                    onItemClick = { onItemClick(babyStatus.id) },
                    onDeleteItemClick = { onDeleteItemClick(babyStatus.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabyStatusListScreenPreview() {
    val babyStatusList = mutableListOf<BabyStatus>()
    for (i in 1..15) {
        babyStatusList.add(
            BabyStatus(
                id = i.toLong(),
                title = "${stringResource(id = R.string.baby_status_label)} $i",
                date = 0L,
                height = 0F,
                weight = 0F
            )
        )
    }

    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BabyStatusContent(
                babyStatusList = babyStatusList,
                onItemClick = {},
                onNavigationIconClick = {},
                onAddButtonClick = {},
                onDeleteItemClick = {}
            )
        }
    }
}