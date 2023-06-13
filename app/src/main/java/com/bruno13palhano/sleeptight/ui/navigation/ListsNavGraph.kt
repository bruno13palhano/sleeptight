package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.ListsScreen

fun NavGraphBuilder.listsNavGraph(navController: NavController) {
    navigation(
        startDestination = ListsDestinations.ALL_LISTS_ROUTE,
        route = SleepTightDestinations.LISTS_ROUTE
    ) {
        composable(route = ListsDestinations.ALL_LISTS_ROUTE) {
            ListsScreen()
        }
        composable(route = ListsDestinations.LISTS_BABY_STATUS_ROUTE) {
            babyStatusNavGraph(navController = navController)
        }
        composable(route = ListsDestinations.LISTS_NAPS_ROUTE) {
            napsNavGraph(navController = navController)
        }
        composable(route = ListsDestinations.LISTS_NOTIFICATIONS_ROUTE) {
            notificationsNavGraph(navController = navController)
        }
    }
}