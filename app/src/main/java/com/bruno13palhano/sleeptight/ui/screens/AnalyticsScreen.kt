package com.bruno13palhano.sleeptight.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.navigation.AnalyticsDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onItemClick: (route: String) -> Unit
) {
    val items = listOf(
        AnalyticsItem.BabyStatusCharts,
        AnalyticsItem.NapCharts
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.analytics_label))} )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach{ analyticsItem ->
                AnalyticsCard(analyticsItem = analyticsItem) {
                    onItemClick(analyticsItem.route)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    val items = listOf(
        AnalyticsItem.BabyStatusCharts,
        AnalyticsItem.NapCharts
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.analytics_label))} )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items.forEach { analyticsItem ->
                AnalyticsCard(analyticsItem = analyticsItem) {

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsCard(
    analyticsItem: AnalyticsItem,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .sizeIn(maxHeight = 128.dp)
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(128.dp),
                imageVector = analyticsItem.imageVector,
                contentDescription = stringResource(id = analyticsItem.text)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(id = analyticsItem.text)
            )
        }
    }
}

sealed class AnalyticsItem(@StringRes val text: Int, val imageVector: ImageVector, val route: String) {
    object BabyStatusCharts: AnalyticsItem(R.string.all_baby_status_label, Icons.Filled.Image, AnalyticsDestinations.BABY_STATUS_CHARTS_ROUTE)
    object NapCharts: AnalyticsItem(R.string.all_naps_chart_label, Icons.Filled.Image, AnalyticsDestinations.NAP_CHARTS_ROUTE)
}