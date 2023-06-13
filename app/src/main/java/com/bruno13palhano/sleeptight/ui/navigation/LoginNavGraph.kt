package com.bruno13palhano.sleeptight.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.loginNavGraph(navController: NavController) {
    navigation(
        startDestination = LoginDestinations.LOGIN_ROUTE,
        route = LoginDestinations.LOGIN_ROUTE
    ) {
        composable(route = LoginDestinations.LOGIN_ROUTE) {

        }
        composable(route = LoginDestinations.CREATE_ACCOUNT_ROUTE) {

        }
        composable(route = LoginDestinations.BABY_PHOTO_ROUTE) {

        }
        composable(route = LoginDestinations.BABY_NAME_ROUTE) {

        }
        composable(route = LoginDestinations.BABY_BIRTHPLACE_ROUTE) {

        }
        composable(route = LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE) {

        }
    }
}