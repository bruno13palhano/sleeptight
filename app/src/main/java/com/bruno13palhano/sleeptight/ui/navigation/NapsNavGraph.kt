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
    viewModelStoreOwner: ViewModelStoreOwner
) {
    navigation(
        startDestination = NapsDestination.NAPS_ROUTE,
        route = ListsDestinations.NAP_LIST_ROUTE
    ) {
        val navActions = NapsNavigationActions(navController)

        composable(route = NapsDestination.NAPS_ROUTE) {
            NapsScreen(
                onItemClick = { napId -> navActions.navigateFromNapsToNap(napId) },
                onAddButtonClick = navActions.navigateFromNapsToNewNapTitleAndObservation,
                onNavigationIconClick = { navController.navigateUp() }
            )
        }
        composable(route = NapsDestination.NAP_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("napId")?.let { napId ->
                NapScreen(
                    napId = napId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE) {
            NewNapTitleAndObservationScreen(
                onNextButtonClick = navActions.navigateFromNewNapTitleAndObservationToDate,
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
        composable(route = NapsDestination.NEW_NAP_DATE_ROUTE) {
            NewNapDateScreen(
                onNextButtonClick = navActions.navigateFromNewNapDateToStartTime,
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
        composable(route = NapsDestination.NEW_NAP_START_TIME_ROUTE) {
            NewNapStartTimeScreen(
                onNextButtonClick = navActions.navigateFromNewNapStartTimeToEndTime,
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
        composable(route = NapsDestination.NEW_NAP_END_TIME_ROUTE) {
            NewNapEndTimeScreen(
                onDoneButtonClick = navActions.navigateFromNewNapEndTimeToNaps,
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
    }
}