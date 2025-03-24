package com.bruno13palhano.sleeptight.ui.screens.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.rememberMarker
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun BabyStatusChartsScreen(
    viewModel: AnalyticsBabyStatusChartViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit,
) {
    val babyStatusChart by viewModel.babyStatusChartUi.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }
    val menuItems = arrayOf(
        stringResource(id = R.string.baby_status_chart_label),
    )

    var chartEntry by remember { mutableStateOf(ChartEntryModelProducer()) }
    LaunchedEffect(key1 = babyStatusChart) {
        chartEntry = babyStatusChart
    }

    BabyStatusChartsContent(
        chartEntry = chartEntry,
        expanded = expanded,
        menuItems = menuItems,
        onExpandedChange = {
            expanded = it
        },
        onMenuItemClick = {
            when (it) {
                BabyStatusChartsMenuItem.ALL_BABY_STATUS_CHART_ITEM_INDEX -> {
                    chartEntry = babyStatusChart
                }
                else -> {}
            }
        },
        onNavigationIconClick = onNavigationIconClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyStatusChartsContent(
    chartEntry: ChartEntryModelProducer,
    expanded: Boolean,
    menuItems: Array<String>,
    onExpandedChange: (expanded: Boolean) -> Unit,
    onMenuItemClick: (index: Int) -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_status_chart_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onExpandedChange(true)
                        },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(
                                    id = R.string.more_options_label,
                                ),
                            )

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { onExpandedChange(false) },
                                ) {
                                    menuItems.forEachIndexed { index, itemValue ->
                                        DropdownMenuItem(
                                            text = { Text(text = itemValue) },
                                            onClick = {
                                                onMenuItemClick(index)
                                                onExpandedChange(false)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            ProvideChartStyle(
                chartStyle = m3ChartStyle(
                    entityColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                    ),
                ),
            ) {
                val marker = rememberMarker()
                Chart(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight(),
                    chart = columnChart(),
                    runInitialAnimation = true,
                    chartModelProducer = chartEntry,
                    startAxis = startAxis(),
                    marker = marker,
                    fadingEdges = rememberFadingEdges(),
                    bottomAxis = bottomAxis(),
                )
            }
        }
    }
}

@Preview
@Composable
fun BabyStatusChartsScreenPreview() {
    BabyStatusChartsContent(
        chartEntry = ChartEntryModelProducer(),
        expanded = false,
        menuItems = arrayOf(),
        onExpandedChange = {},
        onMenuItemClick = {},
        onNavigationIconClick = {},
    )
}

object BabyStatusChartsMenuItem {
    const val ALL_BABY_STATUS_CHART_ITEM_INDEX = 0
}
