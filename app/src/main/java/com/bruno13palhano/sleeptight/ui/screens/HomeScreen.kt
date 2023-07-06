package com.bruno13palhano.sleeptight.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

    when (homeState) {
        HomeViewModel.HomeState.Loading -> {
            CircularProgress()
        }
        HomeViewModel.HomeState.LoggedIn -> {
            Text(text = "Home Screen")
        }
        HomeViewModel.HomeState.NotLoggedIn -> {
            LaunchedEffect(key1 = Unit) {
                navigateToLogin()
            }
        }
    }
}
