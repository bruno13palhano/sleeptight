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
        route = ListsDestinations.LISTS_NAPS_ROUTE
    ) {
        composable(route = NapsDestination.NAPS_ROUTE) {
            NapsScreen()
        }
        composable(route = NapsDestination.NAP_ROUTE) {
            NapScreen()
        }
        composable(route = NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE) {
            NewNapTitleAndObservationScreen()
        }
        composable(route = NapsDestination.NEW_NAP_DATE_ROUTE) {
            NewNapDateScreen()
        }
        composable(route = NapsDestination.NEW_NAP_START_TIME_ROUTE) {
            NewNapStartTimeScreen()
        }
        composable(route = NapsDestination.NEW_NAP_END_TIME_ROUTE) {
            NewNapEndTimeScreen()
        }
    }
}