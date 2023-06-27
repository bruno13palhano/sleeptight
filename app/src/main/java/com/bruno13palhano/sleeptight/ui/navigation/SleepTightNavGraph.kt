package com.bruno13palhano.sleeptight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.sleeptight.ui.screens.HomeScreen
import com.bruno13palhano.sleeptight.ui.screens.PlayerScreen
import com.bruno13palhano.sleeptight.ui.screens.SettingsScreen

@Composable
fun SleepTightNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = SleepTightDestinations.HOME_ROUTE,
    viewModelStoreOwner: ViewModelStoreOwner,
    showBottomMenu: (showMenu: Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        loginNavGraph(navController = navController)

        composable(route = SleepTightDestinations.HOME_ROUTE) {
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(SleepTightDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
                        popUpTo(SleepTightDestinations.HOME_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                showBottomMenu = { showMenu -> showBottomMenu(showMenu) }
            )
        }

        listsNavGraph(navController = navController, viewModelStoreOwner = viewModelStoreOwner)

        composable(route = SleepTightDestinations.PLAYER_ROUTE) {
            PlayerScreen()
        }

        analyticsNavGraph(navController)

        composable(route = SleepTightDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                navigateToLogin = {
                    navController.navigate(SleepTightDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
                        popUpTo(SleepTightDestinations.SETTINGS_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
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