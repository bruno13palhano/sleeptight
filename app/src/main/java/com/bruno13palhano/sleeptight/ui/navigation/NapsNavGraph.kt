package com.bruno13palhano.sleeptight.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.naps.NapScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NapsScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapDateScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapEndTimeScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapStartTimeScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapTitleAndObservationScreen

fun NavGraphBuilder.napsNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation(
        startDestination = NapsDestination.NAPS_ROUTE,
        route = ListsDestinations.NAP_LIST_ROUTE,
    ) {
        composable(route = NapsDestination.NAPS_ROUTE) {
            NapsScreen(
                onItemClick = {
                    navController.navigate(route = "${NapsDestination.NAP_ROUTE}$it") {
                        popUpTo(route = NapsDestination.NAPS_ROUTE)
                    }
                },
                onAddButtonClick = {
                    navController.navigate(
                        route = NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE,
                    ) {
                        popUpTo(route = NapsDestination.NAPS_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
        composable(route = NapsDestination.NAP_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("napId")?.let { napId ->
                NapScreen(
                    napId = napId.toLong(),
                    navigateUp = { navController.navigateUp() },
                )
            }
        }
        composable(route = NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE) {
            NewNapTitleAndObservationScreen(
                onNextButtonClick = {
                    navController.navigate(route = NapsDestination.NEW_NAP_DATE_ROUTE) {
                        popUpTo(route = NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = NapsDestination.NEW_NAP_DATE_ROUTE) {
            NewNapDateScreen(
                onNextButtonClick = {
                    navController.navigate(route = NapsDestination.NEW_NAP_START_TIME_ROUTE) {
                        popUpTo(route = NapsDestination.NEW_NAP_DATE_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = NapsDestination.NEW_NAP_START_TIME_ROUTE) {
            NewNapStartTimeScreen(
                onNextButtonClick = {
                    navController.navigate(route = NapsDestination.NEW_NAP_END_TIME_ROUTE) {
                        popUpTo(route = NapsDestination.NEW_NAP_START_TIME_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = NapsDestination.NEW_NAP_END_TIME_ROUTE) {
            NewNapEndTimeScreen(
                onDoneButtonClick = {
                    navController.navigate(route = NapsDestination.NAPS_ROUTE) {
                        popUpTo(route = NapsDestination.NAPS_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
    }
}

object NapsDestination {
    const val NAPS_ROUTE = "naps"
    const val NAP_ROUTE = "nap/"
    const val NAP_WITH_ID_ROUTE = "$NAP_ROUTE{napId}"
    const val NEW_NAP_TITLE_AND_OBSERVATION_ROUTE = "new_nap_title_and_observation"
    const val NEW_NAP_DATE_ROUTE = "new_nap_date"
    const val NEW_NAP_START_TIME_ROUTE = "new_nap_start_time"
    const val NEW_NAP_END_TIME_ROUTE = "new_nap_end_time"
}
