package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusScreen
import com.bruno13palhano.sleeptight.ui.screens.home.HomeScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NapScreen
import com.bruno13palhano.sleeptight.ui.screens.notifications.NotificationScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
) {
    navigation<MainRoutes.MainHome>(startDestination = HomeRoutes.Home) {
        composable<HomeRoutes.Home> {
            showBottomMenu(true)
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(route = MainRoutes.MainLogin) {
                        popUpTo(route = HomeRoutes.Home) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToLastBabyStatus = { id ->
                    navController.navigate(route = HomeRoutes.LastBabyStatus(id = id)) {
                        popUpTo(route = HomeRoutes.Home)
                    }
                },
                navigateToLastNap = { id ->
                    navController.navigate(route = HomeRoutes.LastNap(id = id)) {
                        popUpTo(route = HomeRoutes.Home)
                    }
                },
                navigateToLastNotification = { id ->
                    navController.navigate(route = HomeRoutes.LastNotification(id = id)) {
                        popUpTo(route = HomeRoutes.Home)
                    }
                },
            )
        }
        composable<HomeRoutes.LastBabyStatus> {
            val id = it.toRoute<HomeRoutes.LastBabyStatus>().id
            BabyStatusScreen(
                babyStatusId = id,
                navigateUp = { navController.navigateUp() },
            )
        }
        composable<HomeRoutes.LastNap> {
            val id = it.toRoute<HomeRoutes.LastNap>().id
            NapScreen(
                napId = id,
                navigateUp = { navController.navigateUp() },
            )
        }
        composable<HomeRoutes.LastNotification> {
            val id = it.toRoute<HomeRoutes.LastNotification>().id
            NotificationScreen(
                notificationId = id,
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}

internal sealed interface HomeRoutes {
    @Serializable
    data object Home : HomeRoutes

    @Serializable
    data class LastBabyStatus(val id: Long) : HomeRoutes

    @Serializable
    data class LastNap(val id: Long) : HomeRoutes

    @Serializable
    data class LastNotification(val id: Long) : HomeRoutes
}
