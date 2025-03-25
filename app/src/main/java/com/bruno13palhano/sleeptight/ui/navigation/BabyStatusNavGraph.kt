package com.bruno13palhano.sleeptight.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusListScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.NewBabyStatusHeightAndWeightScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.NewBabyStatusTitleAndDateScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.babyStatusNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation(
        startDestination = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE,
        route = ListsDestinations.BABY_STATUS_LIST_ROUTE,
    ) {
        composable(route = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE) {
            BabyStatusListScreen(
                onItemClick = { babyStatusId ->
                    navController.navigate(
                        route = "${BabyStatusDestinations.BABY_STATUS_ROUTE}$babyStatusId",
                    ) {
                        popUpTo(route = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE)
                    }
                },
                onAddButtonClick = {
                    navController.navigate(
                        route = BabyStatusDestinations.NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE,
                    ) {
                        popUpTo(route = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
        composable(route = BabyStatusDestinations.BABY_STATUS_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("babyStatusId")?.let { babyStatusId ->
                BabyStatusScreen(
                    babyStatusId = babyStatusId.toLong(),
                    navigateUp = { navController.navigateUp() },
                )
            }
        }
        composable(route = BabyStatusDestinations.NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE) {
            NewBabyStatusTitleAndDateScreen(
                onNextButtonClick = {
                    navController.navigate(
                        route = BabyStatusDestinations.NEW_BABY_STATUS_HEIGHT_AND_WEIGHT_ROUTE,
                    ) {
                        popUpTo(route = BabyStatusDestinations.NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newBabyStatusViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = BabyStatusDestinations.NEW_BABY_STATUS_HEIGHT_AND_WEIGHT_ROUTE) {
            NewBabyStatusHeightAndWeightScreen(
                onDoneButtonClick = {
                    navController.navigate(route = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE) {
                        popUpTo(route = BabyStatusDestinations.ALL_BABY_STATUS_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newBabyStatusViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
    }
}

object BabyStatusDestinations {
    const val ALL_BABY_STATUS_ROUTE = "all_baby_status"
    const val BABY_STATUS_ROUTE = "baby_status/"
    const val BABY_STATUS_WITH_ID_ROUTE = "$BABY_STATUS_ROUTE{babyStatusId}"
    const val NEW_BABY_STATUS_TITLE_AND_DATE_ROUTE = "new_baby_status_title_and_date"
    const val NEW_BABY_STATUS_HEIGHT_AND_WEIGHT_ROUTE = "new_baby_status_height_and_weight"
}

internal sealed interface BabyStatusRoutes {
    @Serializable
    data object AllBabyStatus : BabyStatusRoutes

    @Serializable
    data class BabyStatus(val id: Long) : BabyStatusRoutes

    @Serializable
    data object NewBabyStatusTitleAndDate : BabyStatusRoutes

    @Serializable
    data object NewBabyStatusHeightAndWeight : BabyStatusRoutes
}
