package com.bruno13palhano.sleeptight.ui
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.navigation.SleepTightDestinations

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
        Screen.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(text = stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Home : Screen(SleepTightDestinations.HOME_ROUTE, Icons.Filled.Home, R.string.home_label)
    object Lists : Screen(SleepTightDestinations.LISTS_ROUTE, Icons.Filled.List, R.string.lists_label)
    object Player : Screen(SleepTightDestinations.PLAYER_ROUTE, Icons.Filled.MusicNote, R.string.player_label)
    object Analytics : Screen(SleepTightDestinations.ANALYTICS_ROUTE, Icons.Filled.BarChart, R.string.analytics_label)
    object Settings: Screen(SleepTightDestinations.SETTINGS_ROUTE, Icons.Filled.Settings, R.string.settings_label)
}
