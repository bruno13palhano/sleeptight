package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController

class LoginNavigationActions(navController: NavController) {
    val navigateToLogin: () -> Unit = {
        navController.navigate(LoginDestinations.LOGIN_ROUTE)
    }
    val navigateFromLoginToHome: () -> Unit = {
        navController.navigate(SleepTightDestinations.HOME_ROUTE) {
            popUpTo(LoginDestinations.LOGIN_ROUTE) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    val navigateFromLoginToCreateAccount: () -> Unit = {
        navController.navigate(LoginDestinations.CREATE_ACCOUNT_ROUTE) {
            popUpTo(LoginDestinations.LOGIN_ROUTE)
        }
    }
    val navigateFromCreateAccountToBabyPhoto: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_PHOTO_ROUTE) {
            popUpTo(LoginDestinations.CREATE_ACCOUNT_ROUTE)
        }
    }
    val navigateFromBabyPhotoToBabyName: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_NAME_ROUTE) {
            popUpTo(LoginDestinations.BABY_PHOTO_ROUTE)
        }
    }
    val navigateFromBabyNameToBirthplace: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_BIRTHPLACE_ROUTE) {
            popUpTo(LoginDestinations.BABY_NAME_ROUTE)
        }
    }
    val navigateFromBirthplaceToBirthAccount: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE) {
            popUpTo(LoginDestinations.BABY_BIRTHPLACE_ROUTE)
        }
    }
    val navigateFromBirthAccountToHome: () -> Unit = {
        navController.navigate(SleepTightDestinations.HOME_ROUTE) {
            popUpTo(LoginDestinations.LOGIN_ROUTE) {
                inclusive = true
            }
            launchSingleTop = true
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