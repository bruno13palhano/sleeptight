package com.bruno13palhano.sleeptight.ui.navigation
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bruno13palhano.sleeptight.R

@Composable
fun BottomNavigation(navController: NavHostController) {
    BottomMenu(navController)
}

@Composable
fun BottomMenu(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Lists,
        Screen.Player,
        Screen.Analytics,
        Screen.Settings,
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val selected = currentDestination?.isRouteSelected(screen)

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(id = screen.resourceId),
                    )
                },
                label = { Text(text = stringResource(screen.resourceId)) },
                selected = selected == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

internal fun <T> NavDestination.isRouteSelected(screen: Screen<T>): Boolean {
    val graphPrefixes = mapOf(
        Screen.Home to MainRoutes.MainHome::class.qualifiedName,
        Screen.Lists to MainRoutes.Lists::class.qualifiedName,
        Screen.Player to MainRoutes.Player::class.qualifiedName,
        Screen.Analytics to MainRoutes.Analytics::class.qualifiedName,
        Screen.Settings to MainRoutes.Settings::class.qualifiedName,
    )

    val listsNestedPrefixes = listOf(
        ListsRoutes::class.qualifiedName,
        NapRoutes::class.qualifiedName,
        BabyStatusRoutes::class.qualifiedName,
        NotificationRoutes::class.qualifiedName,
    )

    return hierarchy.any { dest ->
        val destRoute = dest.route
        when (screen) {
            is Screen.Home -> destRoute == HomeRoutes.Home::class.qualifiedName
            is Screen.Lists -> {
                val prefix = graphPrefixes[screen]
                destRoute == prefix ||
                    listsNestedPrefixes.any { nestedPrefix ->
                        destRoute?.startsWith(nestedPrefix ?: "") == true
                    }
            }
            is Screen.Player -> destRoute == MainRoutes.Player::class.qualifiedName
            is Screen.Analytics -> {
                val prefix = graphPrefixes[screen]
                destRoute == prefix ||
                    destRoute?.startsWith(AnalyticsRoutes::class.qualifiedName ?: "") == true
            }
            is Screen.Settings -> destRoute == MainRoutes.Settings::class.qualifiedName
        }
    }
}

internal sealed class Screen<T>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int,
) {
    data object Home : Screen<HomeRoutes>(
        route = HomeRoutes.Home,
        icon = Icons.Filled.Home,
        resourceId = R.string.home_label,
    )
    object Lists : Screen<ListsRoutes>(
        route = ListsRoutes.AllLists,
        icon = Icons.AutoMirrored.Filled.List,
        resourceId = R.string.lists_label,
    )
    object Player : Screen<MainRoutes>(
        route = MainRoutes.Player,
        icon = Icons.Filled.MusicNote,
        resourceId = R.string.player_label,
    )
    object Analytics : Screen<AnalyticsRoutes>(
        route = AnalyticsRoutes.ListCharts,
        icon = Icons.Filled.BarChart,
        resourceId = R.string.analytics_label,
    )
    object Settings : Screen<MainRoutes>(
        route = MainRoutes.Settings,
        icon = Icons.Filled.Settings,
        resourceId = R.string.settings_label,
    )
}
