package com.bruno13palhano.sleeptight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.sleeptight.ui.screens.AnalyticsScreen
import com.bruno13palhano.sleeptight.ui.screens.HomeScreen
import com.bruno13palhano.sleeptight.ui.screens.ListsScreen
import com.bruno13palhano.sleeptight.ui.screens.PlayerScreen
import com.bruno13palhano.sleeptight.ui.screens.SettingsScreen

@Composable
fun SleepTightNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SleepTightDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = SleepTightDestinations.HOME_ROUTE) {
            HomeScreen()
        }

        listsNavGraph(navController)

        composable(route = SleepTightDestinations.PLAYER_ROUTE) {
            PlayerScreen()
        }

        composable(route = SleepTightDestinations.ANALYTICS_ROUTE) {
            AnalyticsScreen()
        }

        composable(route = SleepTightDestinations.SETTINGS_ROUTE) {
            SettingsScreen()
        }
    }
}