package com.bruno13palhano.sleeptight.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val babyInfoState by homeViewModel.babyInfoState.collectAsStateWithLifecycle()
    val babyStatusState by homeViewModel.babyStatusState.collectAsStateWithLifecycle()
    val notificationState by homeViewModel.notificationState.collectAsStateWithLifecycle()
    val napState by homeViewModel.napState.collectAsStateWithLifecycle()

    when (homeState) {
        HomeViewModel.HomeState.Loading -> {
            CircularProgress()
        }
        HomeViewModel.HomeState.LoggedIn -> {
            HomeContent(
                babyName = babyInfoState.babyName,
                profileImage = babyInfoState.profileImage,
                momName = babyInfoState.momName,
                babyTitle = babyStatusState.title,
                babyDate = babyStatusState.date,
                babyHeight = babyStatusState.height,
                babyWeight = babyStatusState.weight,
                notificationTitle = notificationState.title,
                notificationDate = notificationState.date,
                napTitle = napState.title,
                napDate = napState.date,
                napSleepingTime = napState.sleepingTime
            )
        }
        HomeViewModel.HomeState.NotLoggedIn -> {
            LaunchedEffect(key1 = Unit) {
                navigateToLogin()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    babyName: String,
    profileImage: String,
    momName: String,
    babyTitle:String,
    babyDate: String,
    babyHeight: String,
    babyWeight: String,
    notificationTitle: String,
    notificationDate: String,
    napTitle: String,
    napDate: String,
    napSleepingTime: String
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                onClick = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(maxHeight = 368.dp, minHeight = 368.dp),
                        painter = rememberAsyncImagePainter(profileImage),
                        contentDescription = stringResource(id = R.string.baby_photo_label),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.welcome_label, momName, babyName),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                onClick ={}
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    text = stringResource(id = R.string.last_baby_status_label),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    text = babyTitle,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(id = R.string.date_label)
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 16.dp),
                        text = babyDate,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.SquareFoot,
                        contentDescription = stringResource(id = R.string.date_label)
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 16.dp),
                        text = babyHeight,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Balance,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 16.dp),
                        text = babyWeight,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (napTitle != "") {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {}
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        text = stringResource(id = R.string.last_nap_label),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp),
                        text = napTitle,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = stringResource(id = R.string.date_label)
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp, end = 16.dp),
                            text = napDate,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = stringResource(id = R.string.sleeping_time_label)
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp, end = 16.dp),
                            text = napSleepingTime,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            if (notificationTitle != "") {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {}
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        text = stringResource(id = R.string.last_notification_label),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp),
                        text = notificationTitle,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = stringResource(id = R.string.date_label)
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp, end = 16.dp),
                            text = notificationDate,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SleepTightTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeContent(
                babyName = "Baby",
                profileImage = "",
                momName = "Mom",
                babyTitle = "Baby Status Test",
                babyDate = "11/07/2023",
                babyHeight = "34.56cm",
                babyWeight = "3.65kg",
                notificationTitle = "Take a shower",
                notificationDate = "14/07/2023",
                napTitle = "Middle nap",
                napDate = "13/07/2023",
                napSleepingTime = "12:12"
            )
        }
    }
}