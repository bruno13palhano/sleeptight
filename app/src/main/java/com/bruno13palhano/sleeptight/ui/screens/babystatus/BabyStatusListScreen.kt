 package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.R

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
        onAddButtonClick = onAddButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyStatusContent(
    babyStatusList: List<BabyStatus>,
    onItemClick: (id: Long) -> Unit,
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
                ItemBabyTest(title = babyStatus.title) {
                    onItemClick(babyStatus.id)
                }
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
                title = "BabyStatus $i",
                date = 0L,
                height = 0F,
                weight = 0F
            )
        )
    }
    BabyStatusContent(
        babyStatusList = babyStatusList,
        onItemClick = {},
        onNavigationIconClick = {},
        onAddButtonClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemBabyTest(
    title: String,
    onItemClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
        onClick = onItemClick
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = title,
            fontSize = 22.sp
        )
    }
}