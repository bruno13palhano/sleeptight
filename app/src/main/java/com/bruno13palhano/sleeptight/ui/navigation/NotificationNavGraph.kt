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
        route = ListsDestinations.NOTIFICATIONS_LIST_ROUTE,
    ) {
        composable(route = NotificationsDestinations.NOTIFICATIONS_ROUTE) {
            NotificationsScreen(
                onItemClick = { notificationId ->
                    navController.navigate("${NotificationsDestinations.NOTIFICATION_ROUTE}$notificationId") {
                        popUpTo(route = NotificationsDestinations.NOTIFICATIONS_ROUTE)
                    }
                },
                onAddButtonClick = {
                    navController.navigate(route = NotificationsDestinations.NEW_NOTIFICATION_ROUTE) {
                        popUpTo(route = NotificationsDestinations.NOTIFICATIONS_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
        composable(route = NotificationsDestinations.NOTIFICATION_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("notificationId")?.let { notificationId ->
                NotificationScreen(
                    notificationId = notificationId.toLong(),
                    navigateUp = { navController.navigateUp() },
                )
            }
        }
        composable(route = NotificationsDestinations.NEW_NOTIFICATION_ROUTE) {
            NewNotificationScreen(
                onDoneButtonClick = { navController.navigateUp() },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
    }
}

object NotificationsDestinations {
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val NOTIFICATION_ROUTE = "notification/"
    const val NOTIFICATION_WITH_ID_ROUTE = "$NOTIFICATION_ROUTE{notificationId}"
    const val NEW_NOTIFICATION_ROUTE = "new_notification"
}
