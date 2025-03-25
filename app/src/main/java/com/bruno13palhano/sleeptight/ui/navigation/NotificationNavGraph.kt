package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.sleeptight.ui.screens.notifications.NewNotificationScreen
import com.bruno13palhano.sleeptight.ui.screens.notifications.NotificationScreen
import com.bruno13palhano.sleeptight.ui.screens.notifications.NotificationsScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.notificationsNavGraph(navController: NavController) {
    navigation<ListsRoutes.NotificationsList>(startDestination = NotificationRoutes.Notifications) {
        composable<NotificationRoutes.Notifications> {
            NotificationsScreen(
                onItemClick = { id ->
                    navController.navigate(route = NotificationRoutes.Notification(id = id)) {
                        popUpTo(route = NotificationRoutes.Notifications)
                    }
                },
                onAddButtonClick = {
                    navController.navigate(route = NotificationRoutes.NewNotification) {
                        popUpTo(route = NotificationRoutes.Notifications)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
        composable<NotificationRoutes.Notification> {
            val id = it.toRoute<NotificationRoutes.Notification>().id
            NotificationScreen(
                notificationId = id,
                navigateUp = { navController.navigateUp() },
            )
        }
        composable<NotificationRoutes.NewNotification> {
            NewNotificationScreen(
                onDoneButtonClick = { navController.navigateUp() },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
    }
}

internal sealed interface NotificationRoutes {
    @Serializable
    data object Notifications : NotificationRoutes

    @Serializable
    data class Notification(val id: Long) : NotificationRoutes

    @Serializable
    data object NewNotification : NotificationRoutes
}
