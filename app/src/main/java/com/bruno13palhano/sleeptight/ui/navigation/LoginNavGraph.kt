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
        val navActions = LoginNavigationActions(navController)
        composable(route = LoginDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = navActions.navigateFromLoginToHome,
                onCreateAccountButtonClick = navActions.navigateFromLoginToCreateAccount,
            )
        }
        composable(route = LoginDestinations.CREATE_ACCOUNT_ROUTE) {
            CreateAccountScreen(
                onNextButtonClick = navActions.navigateFromCreateAccountToBabyPhoto,
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_PHOTO_ROUTE) {
            BabyPhotoAccountScreen(
                onNextButtonClick = navActions.navigateFromBabyPhotoToBabyName,
                onNavigationIconButton = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_NAME_ROUTE) {
            BabyNameAccountScreen(
                onNextButtonClick = navActions.navigateFromBabyNameToBirthplace,
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_BIRTHPLACE_ROUTE) {
            BabyBirthplaceAccountScreen(
                onNextButtonClick = navActions.navigateFromBirthplaceToBirthAccount,
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
        composable(route = LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE) {
            BabyBirthAccountScreen(
                onCreateAccountSuccess = navActions.navigateFromBirthAccountToHome,
                onNavigationIconClick = { navController.navigateUp() },
                createAccountViewModel = hiltViewModel(viewModelStoreOwner = viewModelStoreOwner),
            )
        }
    }
}
