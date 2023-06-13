package com.bruno13palhano.sleeptight.ui.navigation

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

fun NavGraphBuilder.napsNavGraph(navController: NavController) {
    navigation(
        startDestination = NapsDestination.NAPS_ROUTE,
        route = ListsDestinations.NAP_LIST_ROUTE
    ) {
        val navActions = NapsNavigationActions(navController)
        composable(route = NapsDestination.NAPS_ROUTE) {
            NapsScreen(
                onItemClick = { napId -> navActions.navigateFromNapsToNap(napId) },
                onAddButtonClick = navActions.navigateFromNapsToNewNapTitleAndObservation
            )
        }
        composable(route = NapsDestination.NAP_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("napId")?.let { napId ->
                NapScreen(napId.toLong())
            }
        }
        composable(route = NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE) {
            NewNapTitleAndObservationScreen(
                onNextButtonClick = navActions.navigateFromNewNapTitleAndObservationToDate
            )
        }
        composable(route = NapsDestination.NEW_NAP_DATE_ROUTE) {
            NewNapDateScreen(
                onNextButtonClick = navActions.navigateFromNewNapDateToStartTime
            )
        }
        composable(route = NapsDestination.NEW_NAP_START_TIME_ROUTE) {
            NewNapStartTimeScreen(
                onNextButtonClick = navActions.navigateFromNewNapStartTimeToEndTime
            )
        }
        composable(route = NapsDestination.NEW_NAP_END_TIME_ROUTE) {
            NewNapEndTimeScreen(
                onDoneButtonClick = navActions.navigateFromNewNapEndTimeToNaps
            )
        }
    }
}