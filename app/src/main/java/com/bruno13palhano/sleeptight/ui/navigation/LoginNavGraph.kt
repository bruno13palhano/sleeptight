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

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    navigation(
        startDestination = LoginDestinations.LOGIN_ROUTE,
        route = SleepTightDestinations.LOGIN_CREATE_ACCOUNT_ROUTE,
    ) {
        composable(route = LoginDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(route = SleepTightDestinations.HOME_ROUTE) {
                        popUpTo(route = SleepTightDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onCreateAccountButtonClick = {
                    navController.navigate(route = LoginDestinations.CREATE_ACCOUNT_ROUTE) {
                        popUpTo(route = LoginDestinations.LOGIN_ROUTE)
                    }
                },
            )
        }
        composable(route = LoginDestinations.CREATE_ACCOUNT_ROUTE) {
            CreateAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginDestinations.BABY_PHOTO_ROUTE) {
                        popUpTo(route = LoginDestinations.CREATE_ACCOUNT_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_PHOTO_ROUTE) {
            BabyPhotoAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginDestinations.BABY_NAME_ROUTE) {
                        popUpTo(route = LoginDestinations.BABY_PHOTO_ROUTE)
                    }
                },
                onNavigationIconButton = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_NAME_ROUTE) {
            BabyNameAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginDestinations.BABY_BIRTHPLACE_ROUTE) {
                        popUpTo(route = LoginDestinations.BABY_NAME_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_BIRTHPLACE_ROUTE) {
            BabyBirthplaceAccountScreen(
                onNextButtonClick = {
                    navController.navigate(route = LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE) {
                        popUpTo(route = LoginDestinations.BABY_BIRTHPLACE_ROUTE)
                    }
                },
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE) {
            BabyBirthAccountScreen(
                onCreateAccountSuccess = {
                    navController.navigate(route = SleepTightDestinations.HOME_ROUTE) {
                        popUpTo(route = LoginDestinations.LOGIN_ROUTE) {
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

object LoginDestinations {
    const val LOGIN_ROUTE = "login"
    const val CREATE_ACCOUNT_ROUTE = "create_account"
    const val BABY_PHOTO_ROUTE = "baby_photo"
    const val BABY_NAME_ROUTE = "baby_name"
    const val BABY_BIRTHPLACE_ROUTE = "baby_birthplace"
    const val BABY_BIRTH_ACCOUNT_ROUTE = "baby_birth_account"
}
