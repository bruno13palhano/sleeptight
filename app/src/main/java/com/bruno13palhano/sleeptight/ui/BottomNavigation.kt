package com.bruno13palhano.sleeptight.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.navigation.SleepTightDestinations
import com.bruno13palhano.sleeptight.ui.navigation.SleepTightNavGraph
import com.bruno13palhano.sleeptight.ui.navigation.SleepTightNavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomMenu(navController) }
    ) { paddingValues ->
        SleepTightNavGraph(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
fun BottomMenu(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        Screen.Home,
        Screen.Lists,
        Screen.Player,
        Screen.Analytics,
        Screen.Settings
    )

    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(text = stringResource(screen.resourceId)) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
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
    object Player : Screen(SleepTightDestinations.PLAYER_ROUTE, Icons.Filled.PlayArrow, R.string.player_label)
    object Analytics : Screen(SleepTightDestinations.ANALYTICS_ROUTE, Icons.Filled.Star, R.string.analytics_label)
    object Settings: Screen(SleepTightDestinations.SETTINGS_ROUTE, Icons.Filled.Settings, R.string.settings_label)
}
