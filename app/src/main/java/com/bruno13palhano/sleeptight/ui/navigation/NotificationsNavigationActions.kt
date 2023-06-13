package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController

class NotificationsNavigationActions(navController: NavController) {
    val navigateFromNotificationsToNotification: (notificationId: Long) -> Unit = { notificationId ->
        navController.navigate("${NotificationsDestinations.NOTIFICATION_ROUTE}$notificationId") {
            popUpTo(NotificationsDestinations.NOTIFICATIONS_ROUTE)
        }
    }
    val navigateFromNotificationsToNewNotification: () -> Unit = {
        navController.navigate(NotificationsDestinations.NEW_NOTIFICATION_ROUTE) {
            popUpTo(NotificationsDestinations.NOTIFICATIONS_ROUTE)
        }
    }
}

object NotificationsDestinations {
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val NOTIFICATION_ROUTE = "notification/"
    const val NOTIFICATION_WITH_ID_ROUTE = "$NOTIFICATION_ROUTE{notificationId}"
    const val NEW_NOTIFICATION_ROUTE = "new_notification"
}