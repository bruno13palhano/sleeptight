package com.bruno13palhano.sleeptight.ui.screens.analytics

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MultilineChart
import androidx.compose.material.icons.filled.BarChart
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.navigation.AnalyticsRoutes
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@Composable
internal fun AnalyticsScreen(onItemClick: (route: AnalyticsRoutes) -> Unit) {
    val items = listOf(
        AnalyticsItem.BabyStatusCharts,
        AnalyticsItem.NapCharts,
    )

    AnalyticsContent(
        onItemClick = onItemClick,
        items = items,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalyticsContent(
    onItemClick: (route: AnalyticsRoutes) -> Unit,
    items: List<AnalyticsItem>,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.analytics_label)) })
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            items.forEach { analyticsItem ->
                AnalyticsCard(
                    analyticsItem = analyticsItem,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .weight(1F, true),
                ) {
                    onItemClick(analyticsItem.route)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    val items = listOf(
        AnalyticsItem.BabyStatusCharts,
        AnalyticsItem.NapCharts,
    )

    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AnalyticsContent(
                onItemClick = {},
                items = items,
            )
        }
    }
}

@Composable
private fun AnalyticsCard(
    analyticsItem: AnalyticsItem,
    modifier: Modifier,
    onItemClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = onItemClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier
                    .size(128.dp),
                imageVector = analyticsItem.imageVector,
                contentDescription = stringResource(id = analyticsItem.text),
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(id = analyticsItem.text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

internal sealed class AnalyticsItem(
    @StringRes val text: Int,
    val imageVector: ImageVector,
    val route: AnalyticsRoutes,
) {
    data object BabyStatusCharts : AnalyticsItem(
        text = R.string.all_baby_status_label,
        imageVector = Icons.AutoMirrored.Filled.MultilineChart,
        route = AnalyticsRoutes.BabyStatusCharts,
    )
    data object NapCharts : AnalyticsItem(
        text = R.string.all_naps_chart_label,
        imageVector = Icons.Filled.BarChart,
        route = AnalyticsRoutes.NapCharts,
    )
}
