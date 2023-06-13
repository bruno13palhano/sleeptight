package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController

class BabyStatusNavigationActions(navController: NavController) {
    val navigateFromAllToBabyStatus: (babyStatusId: Long) -> Unit = {
        navController.navigate(BabyStatusDestinations.BABY_STATUS_ROUTE+"$it") {
            popUpTo(BabyStatusDestinations.ALL_BABY_STATUS_ROUTE)
        }
    }
    val navigateFromAllToNewBabyStatusTitleAndDate: () -> Unit = {
        navController.navigate(BabyStatusDestinations.NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE) {
            popUpTo(BabyStatusDestinations.ALL_BABY_STATUS_ROUTE)
        }
    }
    val navigateFromNewBabyStatusTitleAndDateToHeightAndWeight: () -> Unit = {
        navController.navigate(BabyStatusDestinations.NEW_BABY_STATUS_HEIGHT_AND_WEIGHT_ROUTE) {
            popUpTo(BabyStatusDestinations.NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE)
        }
    }
    val navigateFromNewBabyHeightAndWeightToAll: () -> Unit = {
        navController.navigate(BabyStatusDestinations.ALL_BABY_STATUS_ROUTE) {
            popUpTo(BabyStatusDestinations.ALL_BABY_STATUS_ROUTE) {
                inclusive = true
            }
        }
    }
}

object BabyStatusDestinations {
    const val ALL_BABY_STATUS_ROUTE = "all_baby_status"
    const val BABY_STATUS_ROUTE = "baby_status"
    const val NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE = "new_baby_status_title_and_date"
    const val NEW_BABY_STATUS_HEIGHT_AND_WEIGHT_ROUTE = "new_baby_status_height_and_weight"
}