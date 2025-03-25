package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.HomeScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NapScreen
import com.bruno13palhano.sleeptight.ui.screens.notifications.NotificationScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        startDestination = HomeDestinations.HOME_MAIN_ROUTE,
        route = SleepTightDestinations.HOME_ROUTE,
    ) {
        composable(route = HomeDestinations.HOME_MAIN_ROUTE) {
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(SleepTightDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
                        popUpTo(HomeDestinations.HOME_MAIN_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToLastBabyStatus = {
                    navController.navigate("${HomeDestinations.LAST_BABY_STATUS_ROUTE}$it") {
                        popUpTo(HomeDestinations.HOME_MAIN_ROUTE)
                    }
                },
                navigateToLastNap = {
                    navController.navigate("${HomeDestinations.LAST_NAP_ROUTE}$it") {
                        popUpTo(HomeDestinations.HOME_MAIN_ROUTE)
                    }
                },
                navigateToLastNotification = {
                    navController.navigate("${HomeDestinations.LAST_NOTIFICATION_ROUTE}$it") {
                        popUpTo(HomeDestinations.HOME_MAIN_ROUTE)
                    }
                },
            )
        }
        composable(route = HomeDestinations.LAST_BABY_STATUS_ROUTE_WITH_ID) { backStackEntry ->
            backStackEntry.arguments?.getString("babyStatusId")?.let { babyStatusId ->
                BabyStatusScreen(
                    babyStatusId = babyStatusId.toLong(),
                    navigateUp = { navController.navigateUp() },
                )
            }
        }
        composable(route = HomeDestinations.LAST_NAP_ROUTE_WITH_ID) { backStackEntry ->
            backStackEntry.arguments?.getString("napId")?.let { napId ->
                NapScreen(
                    napId = napId.toLong(),
                    navigateUp = { navController.navigateUp() },
                )
            }
        }
        composable(route = HomeDestinations.LAST_NOTIFICATION_ROUTE_WITH_ID) { backStackEntry ->
            backStackEntry.arguments?.getString("notificationId")?.let { notificationId ->
                NotificationScreen(
                    notificationId = notificationId.toLong(),
                    navigateUp = { navController.navigateUp() },
                )
            }
        }
    }
}

object HomeDestinations {
    const val HOME_MAIN_ROUTE = "home_main"
    const val LAST_BABY_STATUS_ROUTE = "home_last_baby_status/"
    const val LAST_NAP_ROUTE = "home_last_nap/"
    const val LAST_NOTIFICATION_ROUTE = "home_last_notification/"
    const val LAST_BABY_STATUS_ROUTE_WITH_ID = "home_last_baby_status/{babyStatusId}"
    const val LAST_NAP_ROUTE_WITH_ID = "home_last_nap/{napId}"
    const val LAST_NOTIFICATION_ROUTE_WITH_ID = "home_last_notification/{notificationId}"
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
