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
        route = ListsDestinations.NOTIFICATIONS_LIST_ROUTE
    ) {
        val navActions = NotificationsNavigationActions(navController)
        composable(route = NotificationsDestinations.NOTIFICATIONS_ROUTE) {
            NotificationsScreen(
                onItemClick = { notificationId ->
                    navActions.navigateFromNotificationsToNotification(notificationId)
                },
                onAddButtonClick = navActions.navigateFromNotificationsToNewNotification,
                onNavigationIconClick = { navController.navigateUp() }
            )
        }
        composable(route = NotificationsDestinations.NOTIFICATION_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("notificationId")?.let { notificationId ->
                NotificationScreen(
                    notificationId = notificationId.toLong(),
                    navigateUp = { navController.navigateUp()}
                )
            }
        }
        composable(route = NotificationsDestinations.NEW_NOTIFICATION_ROUTE) {
            NewNotificationScreen(
                onDoneButtonClick = { navController.navigateUp() },
                onNavigationIconClick = { navController.navigateUp() }
            )
        }
    }
}