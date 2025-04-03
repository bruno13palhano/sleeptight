package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.analytics.AnalyticsScreen
import com.bruno13palhano.sleeptight.ui.screens.analytics.BabyStatusChartsScreen
import com.bruno13palhano.sleeptight.ui.screens.analytics.NapChartsScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.analyticsNavGraph(navController: NavController) {
    navigation<MainRoutes.Analytics>(startDestination = AnalyticsRoutes.ListCharts) {
        composable<AnalyticsRoutes.ListCharts> {
            AnalyticsScreen(
                onItemClick = {
                    navController.navigate(it) {
                        popUpTo(route = AnalyticsRoutes.ListCharts)
                    }
                },
            )
        }

        composable<AnalyticsRoutes.BabyStatusCharts> {
            BabyStatusChartsScreen(
                onNavigationIconClick = { navController.navigateUp() },
            )
        }

        composable<AnalyticsRoutes.NapCharts> {
            NapChartsScreen(
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
    }
}

internal sealed interface AnalyticsRoutes {
    @Serializable
    data object ListCharts : AnalyticsRoutes

    @Serializable
    data object BabyStatusCharts : AnalyticsRoutes

    @Serializable
    data object NapCharts : AnalyticsRoutes
}
