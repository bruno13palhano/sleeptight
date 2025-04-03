package com.bruno13palhano.sleeptight.ui.screens.naps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.shared.ItemList
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil

@Composable
fun NapsScreen(
    onItemClick: (napId: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    napsViewModel: NapsViewModel = hiltViewModel(),
) {
    val napList by napsViewModel.uiState.collectAsStateWithLifecycle()

    NapsContent(
        napList = napList,
        onItemClick = onItemClick,
        onNavigationIconClick = onNavigationIconClick,
        onAddButtonClick = onAddButtonClick,
        onDeleteItemClick = { napId ->
            napsViewModel.deleteNap(napId) {
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NapsContent(
    napList: List<Nap>,
    onItemClick: (id: Long) -> Unit,
    onDeleteItemClick: (id: Long) -> Unit,
    onNavigationIconClick: () -> Unit,
    onAddButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.naps_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            items(
                items = napList,
                key = { nap -> nap.id },
            ) { nap ->
                ItemList(
                    id = nap.id,
                    title = nap.title,
                    date = DateFormatUtil.format(nap.date),
                    onItemClick = { onItemClick(nap.id) },
                    onDeleteItemClick = { onDeleteItemClick(nap.id) },
                )
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
                title = "${stringResource(id = R.string.nap_label)} $i",
                date = 0L,
                startTime = 0L,
                endTime = 0L,
                sleepingTime = 0L,
                observation = "",
            ),
        )
    }
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NapsContent(
                napList = napList,
                onItemClick = {},
                onNavigationIconClick = {},
                onAddButtonClick = {},
                onDeleteItemClick = {},
            )
        }
    }
}
