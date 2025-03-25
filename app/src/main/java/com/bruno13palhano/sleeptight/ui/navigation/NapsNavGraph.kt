package com.bruno13palhano.sleeptight.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.sleeptight.ui.screens.naps.NapScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NapsScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapDateScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapEndTimeScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapStartTimeScreen
import com.bruno13palhano.sleeptight.ui.screens.naps.NewNapTitleAndObservationScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.napsNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation<ListsRoutes.NapList>(startDestination = NapRoutes.Naps) {
        composable<NapRoutes.Naps> {
            NapsScreen(
                onItemClick = {
                    navController.navigate(route = NapRoutes.Nap(id = it)) {
                        popUpTo(route = NapRoutes.Naps)
                    }
                },
                onAddButtonClick = {
                    navController.navigate(route = NapRoutes.NewNapTitleAndObservation) {
                        popUpTo(route = NapRoutes.Naps)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
            )
        }
        composable<NapRoutes.Nap> {
            val id = it.toRoute<NapRoutes.Nap>().id
            NapScreen(
                napId = id,
                navigateUp = { navController.navigateUp() },
            )
        }
        composable<NapRoutes.NewNapTitleAndObservation> {
            NewNapTitleAndObservationScreen(
                onNextButtonClick = {
                    navController.navigate(route = NapRoutes.NewNapDate) {
                        popUpTo(route = NapRoutes.NewNapTitleAndObservation)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<NapRoutes.NewNapDate> {
            NewNapDateScreen(
                onNextButtonClick = {
                    navController.navigate(route = NapRoutes.NewNapStartTime) {
                        popUpTo(route = NapRoutes.NewNapDate)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<NapRoutes.NewNapStartTime> {
            NewNapStartTimeScreen(
                onNextButtonClick = {
                    navController.navigate(route = NapRoutes.NewNapEndTime) {
                        popUpTo(route = NapRoutes.NewNapStartTime)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                newNapViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<NapRoutes.NewNapEndTime> {
            NewNapEndTimeScreen(
                onDoneButtonClick = {
                    navController.navigate(route = NapRoutes.Naps) {
                        popUpTo(route = NapRoutes.Naps) {
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

internal sealed interface NapRoutes {
    @Serializable
    data object Naps : NapRoutes

    @Serializable
    data class Nap(val id: Long) : NapRoutes

    @Serializable
    data object NewNapTitleAndObservation : NapRoutes

    @Serializable
    data object NewNapDate : NapRoutes

    @Serializable
    data object NewNapStartTime : NapRoutes

    @Serializable
    data object NewNapEndTime : NapRoutes
}
