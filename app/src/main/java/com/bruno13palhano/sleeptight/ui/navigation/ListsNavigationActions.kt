package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController

class ListsNavigationActions(navController: NavController) {
    val navigateToBabyStatusList: () -> Unit = {
        navController.navigate(ListsDestinations.BABY_STATUS_LIST_ROUTE) {
            popUpTo(ListsDestinations.ALL_LISTS_ROUTE)
        }
    }
    val navigateToNapsList: () -> Unit = {
        navController.navigate(ListsDestinations.NAP_LIST_ROUTE) {
            popUpTo(ListsDestinations.ALL_LISTS_ROUTE)
        }
    }
    val navigateToNotificationsList: () -> Unit = {
        navController.navigate(ListsDestinations.NOTIFICATIONS_LIST_ROUTE) {
            popUpTo(ListsDestinations.ALL_LISTS_ROUTE)
        }
    }
}

object ListsDestinations {
    const val ALL_LISTS_ROUTE = "all_lists"
    const val BABY_STATUS_LIST_ROUTE = "lists_to_baby_status"
    const val NAP_LIST_ROUTE = "lists_to_naps"
    const val NOTIFICATIONS_LIST_ROUTE = "lists_to_notifications"
}