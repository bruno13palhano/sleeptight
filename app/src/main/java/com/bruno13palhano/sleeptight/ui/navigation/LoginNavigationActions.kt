package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class LoginNavigationActions(navController: NavHostController) {
    val navigateToLogin: () -> Unit = {
        navController.navigate(LoginDestinations.LOGIN_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToCreateAccount: () -> Unit = {
        navController.navigate(LoginDestinations.CREATE_ACCOUNT_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToBabyPhoto: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_PHOTO_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToBabyName: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_NAME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToBirthplace: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_BIRTHPLACE_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToBirthAccount: () -> Unit = {
        navController.navigate(LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
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