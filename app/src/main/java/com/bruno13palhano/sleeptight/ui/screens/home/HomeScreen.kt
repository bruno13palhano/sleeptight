package com.bruno13palhano.sleeptight.ui.screens.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.shared.CircularProgress
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    navigateToLastBabyStatus: (id: Long) -> Unit,
    navigateToLastNap: (id: Long) -> Unit,
    navigateToLastNotification: (id: Long) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
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
                babyId = babyStatusState.id,
                babyDate = babyStatusState.date,
                babyHeight = babyStatusState.height,
                babyWeight = babyStatusState.weight,
                notificationId = notificationState.id,
                notificationTitle = notificationState.title,
                notificationDate = notificationState.date,
                napId = napState.id,
                napTitle = napState.title,
                napDate = napState.date,
                napSleepingTime = napState.sleepingTime,
                navigateToLastBabyStatus = navigateToLastBabyStatus,
                navigateToLastNap = navigateToLastNap,
                navigateToLastNotification = navigateToLastNotification,
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
    babyId: Long,
    babyDate: String,
    babyHeight: String,
    babyWeight: String,
    notificationId: Long,
    notificationTitle: String,
    notificationDate: String,
    napId: Long,
    napTitle: String,
    napDate: String,
    napSleepingTime: String,
    navigateToLastBabyStatus: (id: Long) -> Unit,
    navigateToLastNap: (id: Long) -> Unit,
    navigateToLastNotification: (id: Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.inverseOnSurface,
                        RoundedCornerShape(8.dp),
                    ),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 56.dp)
                            .sizeIn(maxHeight = 368.dp, minHeight = 368.dp),
                        painter = painterResource(id = R.drawable.logo_1),
                        contentDescription = stringResource(id = R.string.baby_photo_label),
                        contentScale = ContentScale.Crop,
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(.75F)
                            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                        text = stringResource(id = R.string.welcome_label, momName, babyName),
                        style = MaterialTheme.typography.titleLarge,
                        fontStyle = FontStyle.Italic,
                    )

                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .border(2.dp, MaterialTheme.colorScheme.inverseOnSurface, CircleShape),
                        contentScale = ContentScale.Crop,
                        painter = rememberAsyncImagePainter(profileImage),
                        contentDescription = stringResource(id = R.string.baby_photo_label),
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                ),
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 4.dp, end = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        if (babyId != 0L) {
                            navigateToLastBabyStatus(babyId)
                        }
                    },
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(
                            id = R.string.home_last_measurements_label,
                            babyDate,
                            babyHeight,
                            babyWeight,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                if (napTitle != "") {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            navigateToLastNap(napId)
                        },
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp),
                            text = stringResource(
                                id = R.string.home_last_nap_label,
                                napDate,
                                napSleepingTime,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }

                if (notificationTitle != "") {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            navigateToLastNotification(notificationId)
                        },
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp),
                            text = stringResource(
                                id = R.string.home_last_notification_label,
                                notificationDate,
                                notificationTitle,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomePreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            HomeContent(
                babyName = "Baby",
                profileImage = "",
                momName = "Mom",
                babyId = 0L,
                babyDate = "11/07/2023",
                babyHeight = "34.56cm",
                babyWeight = "3.65kg",
                notificationId = 0L,
                notificationTitle = "Take a shower",
                notificationDate = "14/07/2023",
                napId = 0L,
                napTitle = "Middle nap",
                napDate = "13/07/2023",
                napSleepingTime = "12:12",
                navigateToLastBabyStatus = {},
                navigateToLastNap = {},
                navigateToLastNotification = {},
            )
        }
    }
}
