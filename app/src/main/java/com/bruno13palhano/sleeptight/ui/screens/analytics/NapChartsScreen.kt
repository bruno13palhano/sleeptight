package com.bruno13palhano.sleeptight.ui.screens.analytics

import android.graphics.Paint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.sleeptight.R
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.decoration.Decoration
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.formatter.ValueFormatter
import com.patrykandpatrick.vico.core.legend.Legend

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NapChartsScreen(
    onNavigationIconClick: () -> Unit,
    viewModel: AnalyticsNapChartViewModel = hiltViewModel()
) {
    val allNaps by viewModel.allNapChartUi.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.all_nap_charts_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options_label)
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                (chartValues.chartEntryModel.entries.first().getOrNull(value.toInt()) as? NapChartEntry)
                    ?.date
                    .orEmpty()
            }

            ProvideChartStyle(m3ChartStyle()) {
                val label by remember { mutableStateOf(TextComponent.Builder()
                    .build()) }
                Chart(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight(),
                    chart = columnChart(
                        dataLabel = label,
                        dataLabelVerticalPosition = VerticalPosition.Bottom,
                        decorations = listOf(
                            ThresholdLine(
                                thresholdValue = 2f,
                                lineComponent = shapeComponent(color = Color.Black),
                                labelComponent = textComponent(Color.Black, padding = dimensionsOf(horizontal = 8.dp)),
                            )
                        )
                    ),
                    runInitialAnimation = true,
                    chartModelProducer = allNaps,
                    startAxis = startAxis(),
                    marker = MarkerComponent(label, null, null),
                    bottomAxis = if (allNaps.getModel().entries.isEmpty()) {
                        bottomAxis()
                    } else {
                        bottomAxis(valueFormatter = axisValueFormatter)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NapChartsScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.all_nap_charts_label)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options_label)
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val chartEntryModelProducer = ChartEntryModelProducer(listOf<FloatEntry>(
                FloatEntry(1F, 3.3F),
                FloatEntry(2F, 1.3F),
                FloatEntry(3F, 2.3F),
                FloatEntry(4F, 8.3F),
                FloatEntry(5F, 6.3F)
            ))

            ProvideChartStyle(m3ChartStyle()) {
                Chart(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight(),
                    chart = columnChart(),
                    chartModelProducer = chartEntryModelProducer,
                    startAxis = startAxis(),
                    bottomAxis = bottomAxis(),
                )
            }
        }
    }
}