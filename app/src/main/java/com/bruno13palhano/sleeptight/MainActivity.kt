package com.bruno13palhano.sleeptight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.sleeptight.ui.navigation.BottomMenu
import com.bruno13palhano.sleeptight.ui.navigation.BottomNavigation
import com.bruno13palhano.sleeptight.ui.navigation.SleepTightNavGraph
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SleepTightTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    var showBottomBar by rememberSaveable { mutableStateOf(false) }

                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = showBottomBar,
                                enter = slideInVertically(
                                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                    initialOffsetY = { it / 8 },
                                ),
                                exit = slideOutVertically(
                                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                    targetOffsetY = { it / 8 },
                                ),
                            ) {
                                BottomMenu(navController = navController)
                            }
                        },
                    ) { paddingValues ->
                        SleepTightNavGraph(
                            modifier = Modifier.padding(paddingValues),
                            navController = navController,
                            showBottomMenu = { show -> showBottomBar = show },
                            viewModelStoreOwner = this,
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
            color = MaterialTheme.colorScheme.background,
        ) {
            Scaffold(
                bottomBar = {
                    BottomNavigation(navController = navController)
                },
            ) { paddingValues ->
                LocalViewModelStoreOwner.current?.let {
                    SleepTightNavGraph(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        showBottomMenu = {},
                        viewModelStoreOwner = it,
                    )
                }
            }
        }
    }
}
