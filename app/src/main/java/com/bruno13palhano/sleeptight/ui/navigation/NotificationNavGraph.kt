package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.notifications.NewNotificationScreen
import com.bruno13palhano.sleeptight.ui.screens.notifications.NotificationScreen
import com.bruno13palhano.sleeptight.ui.screens.notifications.NotificationsScreen

fun NavGraphBuilder.notificationsNavGraph(navController: NavController) {
    navigation(
        startDestination = NotificationsDestinations.NOTIFICATIONS_ROUTE,
        route = ListsDestinations.LISTS_NOTIFICATIONS_ROUTE
    ) {
        composable(route = NotificationsDestinations.NOTIFICATIONS_ROUTE) {
            NotificationsScreen()
        }
        composable(route = NotificationsDestinations.NOTIFICATION_ROUTE) {
            NotificationScreen()
        }
        composable(route = NotificationsDestinations.NEW_NOTIFICATION_ROUTE) {
            NewNotificationScreen()
        }
    }
}