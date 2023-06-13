package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.ListsScreen

fun NavGraphBuilder.listsNavGraph(navController: NavController) {
    navigation(
        startDestination = ListsDestinations.ALL_LISTS_ROUTE,
        route = SleepTightDestinations.LISTS_ROUTE
    ) {
        val navActions = ListsNavigationActions(navController)
        composable(route = ListsDestinations.ALL_LISTS_ROUTE) {
            ListsScreen(
                onBabyStatusClick = { navActions.navigateToBabyStatusList() },
                onNapsClick = { navActions.navigateToNapsList() },
                onNotificationsClick = { navActions.navigateToNotificationsList() }
            )
        }

        babyStatusNavGraph(navController = navController)
        napsNavGraph(navController = navController)
        notificationsNavGraph(navController = navController)
    }
}