package com.bruno13palhano.sleeptight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.sleeptight.ui.screens.AnalyticsScreen
import com.bruno13palhano.sleeptight.ui.screens.HomeScreen
import com.bruno13palhano.sleeptight.ui.screens.PlayerScreen
import com.bruno13palhano.sleeptight.ui.screens.SettingsScreen

@Composable
fun SleepTightNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SleepTightDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        loginNavGraph(navController)

        composable(route = SleepTightDestinations.HOME_ROUTE) {
            HomeScreen {
                if (it)
                    navController.navigate(SleepTightDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
                        popUpTo(SleepTightDestinations.HOME_ROUTE) {
                            inclusive = true
                        }
                    }
            }
        }

        listsNavGraph(navController)

        composable(route = SleepTightDestinations.PLAYER_ROUTE) {
            PlayerScreen()
        }

        analyticsNavGraph(navController)

        composable(route = SleepTightDestinations.SETTINGS_ROUTE) {
            SettingsScreen()
        }
    }
}

object SleepTightDestinations {
    const val HOME_ROUTE = "home"
    const val LISTS_ROUTE = "lists"
    const val PLAYER_ROUTE = "player"
    const val ANALYTICS_ROUTE = "analytics"
    const val SETTINGS_ROUTE = "settings"
    const val LOGIN_CREATE_ACCOUNT_ROUTE = "login_create_account"
}