package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.AnalyticsScreen
import com.bruno13palhano.sleeptight.ui.screens.analytics.BabyStatusChartsScreen
import com.bruno13palhano.sleeptight.ui.screens.analytics.NapChartsScreen

fun NavGraphBuilder.analyticsNavGraph(navController: NavController) {
    navigation(
        startDestination = AnalyticsDestinations.LIST_CHARTS,
        route = SleepTightDestinations.ANALYTICS_ROUTE
    ) {
        composable(route = AnalyticsDestinations.LIST_CHARTS) {
            AnalyticsScreen(
                onItemClick = {
                    navController.navigate(it) {
                        popUpTo(AnalyticsDestinations.LIST_CHARTS)
                    }
                }
            )
        }

        composable(route = AnalyticsDestinations.BABY_STATUS_CHARTS_ROUTE) {
            BabyStatusChartsScreen(
                onNavigationIconClick = { navController.navigateUp() }
            )
        }

        composable(route = AnalyticsDestinations.NAP_CHARTS_ROUTE) {
            NapChartsScreen(
                onNavigationIconClick = { navController.navigateUp() }
            )
        }
    }
}

object AnalyticsDestinations {
    const val LIST_CHARTS = "list_charts"
    const val BABY_STATUS_CHARTS_ROUTE = "baby_status_charts"
    const val NAP_CHARTS_ROUTE = "nap_charts"
}