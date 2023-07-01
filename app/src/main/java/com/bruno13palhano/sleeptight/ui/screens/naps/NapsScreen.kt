package com.bruno13palhano.sleeptight.ui.screens.naps

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
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.R

@Composable
fun NapsScreen(
    onItemClick: (napId: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    napsViewModel: NapsViewModel = hiltViewModel()
) {
    val napList by napsViewModel.uiState.collectAsStateWithLifecycle()

    NapsContent(
        napList = napList,
        onItemClick = onItemClick,
        onNavigationIconClick = onNavigationIconClick,
        onAddButtonClick = onAddButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NapsContent(
    napList: List<Nap>,
    onItemClick: (id: Long) -> Unit,
    onNavigationIconClick: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.naps_label)) },
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
                items = napList,
                key = { nap -> nap.id }
            ) { nap ->
                ItemTest(title = nap.title) {
                    onItemClick(nap.id)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NapsScreenPreview() {
    val napList = mutableListOf<Nap>()
    for (i in 1..15) {
        napList.add(
            Nap(
                id = i.toLong(),
                title = "Naps $i",
                date = 0L,
                startTime = 0L,
                endTime = 0L,
                sleepingTime = 0L,
                observation = ""
            )
        )
    }
    NapsContent(
        napList = napList,
        onItemClick = {},
        onNavigationIconClick = {},
        onAddButtonClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTest(
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