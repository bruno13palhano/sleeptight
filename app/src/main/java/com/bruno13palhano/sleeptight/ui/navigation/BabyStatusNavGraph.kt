package com.bruno13palhano.sleeptight.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusListScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.BabyStatusScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.NewBabyStatusHeightAndWeightScreen
import com.bruno13palhano.sleeptight.ui.screens.babystatus.NewBabyStatusTitleAndDateScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.babyStatusNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation<ListsRoutes.BabyStatusList>(startDestination = BabyStatusRoutes.AllBabyStatus) {
        composable<BabyStatusRoutes.AllBabyStatus> {
            BabyStatusListScreen(
                onItemClick = { id ->
                    navController.navigate(route = BabyStatusRoutes.BabyStatus(id = id)) {
                        popUpTo(route = BabyStatusRoutes.AllBabyStatus)
                    }
                },
                onAddButtonClick = {
                    navController.navigate(route = BabyStatusRoutes.NewBabyStatusTitleAndDate) {
                        popUpTo(route = BabyStatusRoutes.AllBabyStatus)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
        composable<BabyStatusRoutes.BabyStatus> {
            val id = it.toRoute<BabyStatusRoutes.BabyStatus>().id
            BabyStatusScreen(
                babyStatusId = id,
                navigateUp = { navController.navigateUp() },
            )
        }
        composable<BabyStatusRoutes.NewBabyStatusTitleAndDate> {
            NewBabyStatusTitleAndDateScreen(
                onNextButtonClick = {
                    navController.navigate(route = BabyStatusRoutes.NewBabyStatusHeightAndWeight) {
                        popUpTo(route = BabyStatusRoutes.NewBabyStatusTitleAndDate)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newBabyStatusViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<BabyStatusRoutes.NewBabyStatusHeightAndWeight> {
            NewBabyStatusHeightAndWeightScreen(
                onDoneButtonClick = {
                    navController.navigate(route = BabyStatusRoutes.AllBabyStatus) {
                        popUpTo(route = BabyStatusRoutes.AllBabyStatus) {
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
