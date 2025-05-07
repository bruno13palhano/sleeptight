package com.bruno13palhano.sleeptight.ui.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.bruno13palhano.sleeptight.ui.screens.player.PlayerScreen
import com.bruno13palhano.sleeptight.ui.screens.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Composable
internal fun SleepTightNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showBottomMenu: (show: Boolean) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    NavHost(
        navController = navController,
        startDestination = MainRoutes.MainHome,
        modifier = modifier,
    ) {
        loginNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            viewModelStoreOwner = viewModelStoreOwner,
        )

        homeNavGraph(navController = navController, showBottomMenu = showBottomMenu)

        listsNavGraph(
            navController = navController,
            viewModelStoreOwner = viewModelStoreOwner,
            showBottomMenu = showBottomMenu,
        )

        composable<MainRoutes.Player>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "sleeptight://main/player"
                    action = Intent.ACTION_VIEW
                },
            ),
        ) {
            showBottomMenu(true)
            PlayerScreen()
        }

        analyticsNavGraph(navController = navController)

        composable<MainRoutes.Settings> {
            SettingsScreen(
                navigateToLogin = {
                    navController.navigate(route = MainRoutes.MainLogin) {
                        popUpTo(route = MainRoutes.Settings) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

internal sealed interface MainRoutes {
    @Serializable
    data object MainHome : MainRoutes

    @Serializable
    data object Lists : MainRoutes

    @Serializable
    data object Player : MainRoutes

    @Serializable
    data object Analytics : MainRoutes

    @Serializable
    data object Settings : MainRoutes

    @Serializable
    data object MainLogin : MainRoutes
}
