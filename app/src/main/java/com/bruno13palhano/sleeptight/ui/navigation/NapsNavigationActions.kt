package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController

class NapsNavigationActions(navController: NavController) {
    val navigateFromNapsToNap: (napId: Long) -> Unit = {
        navController.navigate(NapsDestination.NAP_ROUTE+"/$it") {
            popUpTo(NapsDestination.NAPS_ROUTE)
        }
    }
    val navigateFromNapsToNewNapTitleAndObservation: () -> Unit = {
        navController.navigate(NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE) {
            popUpTo(NapsDestination.NAPS_ROUTE)
        }
    }
    val navigateFromNewNapTitleAndObservationToDate: () -> Unit = {
        navController.navigate(NapsDestination.NEW_NAP_DATE_ROUTE) {
            popUpTo(NapsDestination.NEW_NAP_TITLE_AND_OBSERVATION_ROUTE)
        }
    }
    val navigateFromNewNapDateToStartTime: () -> Unit = {
        navController.navigate(NapsDestination.NEW_NAP_START_TIME_ROUTE) {
            popUpTo(NapsDestination.NEW_NAP_DATE_ROUTE)
        }
    }
    val navigateFromNewNapStartTimeToEndTime: () -> Unit = {
        navController.navigate(NapsDestination.NEW_NAP_END_TIME_ROUTE) {
            popUpTo(NapsDestination.NEW_NAP_START_TIME_ROUTE)
        }
    }
    val navigateFromNewNapEndTimeToNaps: () -> Unit = {
        navController.navigate(NapsDestination.NAPS_ROUTE) {
            popUpTo(NapsDestination.NAPS_ROUTE) {
                inclusive = true
            }
        }
    }
}

object NapsDestination {
    const val NAPS_ROUTE = "naps"
    const val NAP_ROUTE = "nap"
    const val NEW_NAP_TITLE_AND_OBSERVATION_ROUTE = "new_nap_title_and_observation"
    const val NEW_NAP_DATE_ROUTE = "new_nap_date"
    const val NEW_NAP_START_TIME_ROUTE = "new_nap_start_time"
    const val NEW_NAP_END_TIME_ROUTE = "new_nap_end_time"
}