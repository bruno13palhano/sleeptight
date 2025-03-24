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
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun NapChartsScreen(
    onNavigationIconClick: () -> Unit,
    viewModel: AnalyticsNapChartViewModel = hiltViewModel(),
) {
    val allNaps by viewModel.allNapChartUi.collectAsStateWithLifecycle()
    val monthChart by viewModel.monthNapChartUi.collectAsStateWithLifecycle()
    val weekChart by viewModel.weekNapChartUi.collectAsStateWithLifecycle()
    val shiftChart by viewModel.shiftNapChartUi.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }
    val menuItems = arrayOf(
        stringResource(id = R.string.all_nap_charts_label),
        stringResource(id = R.string.month_chart_label),
        stringResource(id = R.string.week_chart_label),
        stringResource(id = R.string.shift_chart_label),
    )

    var chartEntry by remember { mutableStateOf(ChartEntryModelProducer()) }
    LaunchedEffect(key1 = allNaps) {
        chartEntry = allNaps
    }

    NapChartsContent(
        chartEntry = chartEntry,
        expanded = expanded,
        menuItems = menuItems,
        onExpandedChange = {
            expanded = it
        },
        onMenuItemClick = {
            when (it) {
                NapChartsMenuIndex.ALL_NAPS_CHART_ITEM_INDEX -> {
                    chartEntry = allNaps
                }
                NapChartsMenuIndex.NAP_MONTH_CHART_ITEM_INDEX -> {
                    chartEntry = monthChart
                }
                NapChartsMenuIndex.NAP_WEEK_CHART_ITEM_INDEX -> {
                    chartEntry = weekChart
                }
                NapChartsMenuIndex.NAP_SHIFT_CHART_ITEM_INDEX -> {
                    chartEntry = shiftChart
                }
                else -> {}
            }
        },
        onNavigationIconClick = onNavigationIconClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NapChartsContent(
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
                title = { Text(text = stringResource(id = R.string.all_nap_charts_label)) },
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
            val axisValueFormatter =
                AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                    try {
                        (
                            chartValues.chartEntryModel.entries.first()
                                .getOrNull(value.toInt()) as? NapChartEntry
                            )
                            ?.date
                            .orEmpty()
                    } catch (ignored: Exception) {
                        "0"
                    }
                }

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
                    bottomAxis = if (chartEntry.getModel().entries.isEmpty()) {
                        bottomAxis()
                    } else {
                        bottomAxis(guideline = null, valueFormatter = axisValueFormatter)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun NapChartsScreenPreview() {
    NapChartsContent(
        chartEntry = ChartEntryModelProducer(),
        expanded = false,
        menuItems = arrayOf(),
        onExpandedChange = {},
        onMenuItemClick = {},
        onNavigationIconClick = {},
    )
}

object NapChartsMenuIndex {
    const val ALL_NAPS_CHART_ITEM_INDEX = 0
    const val NAP_MONTH_CHART_ITEM_INDEX = 1
    const val NAP_WEEK_CHART_ITEM_INDEX = 2
    const val NAP_SHIFT_CHART_ITEM_INDEX = 3
}
