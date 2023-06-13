package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class SleepTightNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(SleepTightDestinations.HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToLists: () -> Unit = {
        navController.navigate(SleepTightDestinations.LISTS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToPlayer: () -> Unit = {
        navController.navigate(SleepTightDestinations.PLAYER_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToAnalytics: () -> Unit = {
        navController.navigate(SleepTightDestinations.ANALYTICS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(SleepTightDestinations.SETTINGS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

object SleepTightDestinations {
    const val HOME_ROUTE = "home"
    const val LISTS_ROUTE = "lists"
    const val PLAYER_ROUTE = "player"
    const val ANALYTICS_ROUTE = "analytics"
    const val SETTINGS_ROUTE = "settings"
}