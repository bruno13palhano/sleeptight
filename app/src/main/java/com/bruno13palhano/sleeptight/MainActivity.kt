package com.bruno13palhano.sleeptight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.sleeptight.ui.BottomNavigation
import com.bruno13palhano.sleeptight.ui.navigation.HomeDestinations
import com.bruno13palhano.sleeptight.ui.navigation.LoginDestinations
import com.bruno13palhano.sleeptight.ui.navigation.SleepTightNavGraph
import com.bruno13palhano.sleeptight.ui.screens.CircularProgress
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SleepTightTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var showBottomBar by rememberSaveable { mutableStateOf(false) }
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val mainViewModel: MainViewModel = hiltViewModel()

                    showBottomBar = when (navBackStackEntry?.destination?.route) {
                        LoginDestinations.LOGIN_ROUTE -> false
                        LoginDestinations.CREATE_ACCOUNT_ROUTE -> false
                        LoginDestinations.BABY_NAME_ROUTE -> false
                        LoginDestinations.BABY_PHOTO_ROUTE -> false
                        LoginDestinations.BABY_BIRTHPLACE_ROUTE -> false
                        LoginDestinations.BABY_BIRTH_ACCOUNT_ROUTE -> false
                        HomeDestinations.HOME_MAIN_ROUTE -> {
                            mainViewModel.isUserAuthenticated()
                        }
                        else -> true
                    }

                    Scaffold(
                        bottomBar = {
                            if (navBackStackEntry == null) {
                                CircularProgress()
                            } else {
                                if (showBottomBar)
                                    BottomNavigation(navController = navController)
                            }
                        }
                    ) { paddingValues ->
                        SleepTightNavGraph(
                            modifier = Modifier.padding(paddingValues),
                            navController = navController,
                            viewModelStoreOwner = this
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SleetTightTest() {
    val navController = rememberNavController()
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    BottomNavigation(navController = navController)
                }
            ) { paddingValues ->
                LocalViewModelStoreOwner.current?.let {
                    SleepTightNavGraph(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        viewModelStoreOwner = it,
                    )
                }
            }
        }
    }
}