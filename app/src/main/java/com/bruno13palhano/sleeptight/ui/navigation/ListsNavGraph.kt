package com.bruno13palhano.sleeptight.ui.navigation

import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.ListsScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.listsNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation<MainRoutes.Lists>(startDestination = ListsRoutes.AllLists) {
        composable<ListsRoutes.AllLists> {
            ListsScreen(
                onItemClick = {
                    navController.navigate(it)
                },
            )
        }

        babyStatusNavGraph(navController = navController, viewModelStoreOwner = viewModelStoreOwner)
        napsNavGraph(navController = navController, viewModelStoreOwner = viewModelStoreOwner)
        notificationsNavGraph(navController = navController)
    }
}

object ListsDestinations {
    const val ALL_LISTS_ROUTE = "all_lists"
    const val BABY_STATUS_LIST_ROUTE = "lists_to_baby_status"
    const val NAP_LIST_ROUTE = "lists_to_naps"
    const val NOTIFICATIONS_LIST_ROUTE = "lists_to_notifications"
}

internal sealed interface ListsRoutes {
    @Serializable
    data object AllLists : ListsRoutes

    @Serializable
    data object BabyStatusList : ListsRoutes

    @Serializable
    data object NapList : ListsRoutes

    @Serializable
    data object NotificationsList : ListsRoutes
}
