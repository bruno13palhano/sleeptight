package com.bruno13palhano.sleeptight.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.babystatus.NewBabyStatusHeightAndWeightScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusListScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.NewBabyStatusTitleAndDateScreen

fun NavGraphBuilder.babyStatusNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    navigation(
        startDestination = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE,
        route = ListsDestinations.BABY_STATUS_LIST_ROUTE
    ) {
        val navActions = BabyStatusNavigationActions(navController)
        composable(route = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE) {
            BabyStatusListScreen(
                onItemClick = { babyStatusId ->
                    navActions.navigateFromAllToBabyStatus(babyStatusId)
                },
                onAddButtonClick = navActions.navigateFromAllToNewBabyStatusTitleAndDate,
                onNavigationIconClick = { navController.navigateUp() }
            )
        }
        composable(route = BabyStatusDestinations.BABY_STATUS_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("babyStatusId")?.let { babyStatusId ->
                BabyStatusScreen(
                    babyStatusId = babyStatusId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = BabyStatusDestinations.NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE) {
            NewBabyStatusTitleAndDateScreen(
                onNextButtonClick = navActions.navigateFromNewBabyStatusTitleAndDateToHeightAndWeight,
                onNavigationIconClick = { navController.navigateUp() },
                newBabyStatusViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
        composable(route = BabyStatusDestinations.NEW_BABY_STATUS_HEIGHT_AND_WEIGHT_ROUTE) {
            NewBabyStatusHeightAndWeightScreen(
                onDoneButtonClick = navActions.navigateFromNewBabyHeightAndWeightToAll,
                onNavigationIconClick = { navController.navigateUp() },
                newBabyStatusViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
    }
}