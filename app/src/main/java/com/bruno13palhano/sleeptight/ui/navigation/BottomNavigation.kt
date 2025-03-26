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
    return this.hierarchy.any {
        it.route?.split(".")?.lastOrNull() == screen.route.toString()
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
