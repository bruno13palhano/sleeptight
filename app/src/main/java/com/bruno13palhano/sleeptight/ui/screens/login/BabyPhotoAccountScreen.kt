package com.bruno13palhano.sleeptight.ui.screens.login

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.util.getBytes
import java.io.IOException
import kotlin.jvm.Throws

@Composable
fun BabyPhotoAccountScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconButton: () -> Unit,
    createAccountViewModel: CreateAccountViewModel
) {
    val context = LocalContext.current

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                createAccountViewModel.updatePhotoUri(it)
                getBytes(context, it)?.let { imageByteArray ->
                    createAccountViewModel.updatePhotoByteArray(imageByteArray)
                }
            }
        }

    BabyPhotoAccountContent(
        photoUri = createAccountViewModel.photoUri,
        onPhotoClick = { galleryLauncher.launch("image/*") },
        onNavigationIconButton = onNavigationIconButton,
        onNextButtonClick = onNextButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyPhotoAccountContent(
    photoUri: Uri,
    onPhotoClick: () -> Unit,
    onNavigationIconButton: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_photo_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconButton) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNextButtonClick) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(id = R.string.next_label)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .clickable { onPhotoClick() },
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = stringResource(id = R.string.baby_photo_label)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabyPhotoAccountScreen() {
    BabyPhotoAccountContent(
        photoUri = Uri.EMPTY,
        onPhotoClick = {},
        onNavigationIconButton = {},
        onNextButtonClick = {}
    )
}