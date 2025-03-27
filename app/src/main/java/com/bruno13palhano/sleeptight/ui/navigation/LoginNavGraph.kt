package com.bruno13palhano.sleeptight.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.sleeptight.ui.screens.login.BabyBirthAccountScreen
import com.bruno13palhano.sleeptight.ui.screens.login.BabyBirthplaceAccountScreen
import com.bruno13palhano.sleeptight.ui.screens.login.BabyNameAccountScreen
import com.bruno13palhano.sleeptight.ui.screens.login.BabyPhotoAccountScreen
import com.bruno13palhano.sleeptight.ui.screens.login.CreateAccountScreen
import com.bruno13palhano.sleeptight.ui.screens.login.LoginScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation<MainRoutes.MainLogin>(startDestination = LoginRoutes.Login) {
        composable<LoginRoutes.Login> {
            showBottomMenu(false)
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(route = MainRoutes.MainHome) {
                        popUpTo(route = MainRoutes.MainLogin) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onCreateAccountButtonClick = {
                    navController.navigate(route = LoginRoutes.CreateAccount) {
                        popUpTo(route = LoginRoutes.Login)
                    }
                },
            )
        }
        composable<LoginRoutes.CreateAccount> {
            showBottomMenu(false)
            CreateAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginRoutes.BabyPhoto) {
                        popUpTo(route = LoginRoutes.CreateAccount)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<LoginRoutes.BabyPhoto> {
            BabyPhotoAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginRoutes.BabyName) {
                        popUpTo(route = LoginRoutes.BabyPhoto)
                    }
                },
                onNavigationIconButton = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<LoginRoutes.BabyName> {
            BabyNameAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginRoutes.BabyBirthplace) {
                        popUpTo(route = LoginRoutes.BabyName)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<LoginRoutes.BabyBirthplace> {
            BabyBirthplaceAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginRoutes.BabyBirthAccount) {
                        popUpTo(route = LoginRoutes.BabyBirthplace)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable<LoginRoutes.BabyBirthAccount> {
            BabyBirthAccountScreen(
                onCreateAccountSuccess = {
                    navController.navigate(route = MainRoutes.MainHome) {
                        popUpTo(route = LoginRoutes.Login) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
    }
}

internal sealed interface LoginRoutes {
    @Serializable
    data object Login : LoginRoutes

    @Serializable
    data object CreateAccount : LoginRoutes

    @Serializable
    data object BabyPhoto : LoginRoutes

    @Serializable
    data object BabyName : LoginRoutes

    @Serializable
    data object BabyBirthplace : LoginRoutes

    @Serializable
    data object BabyBirthAccount : LoginRoutes
}
