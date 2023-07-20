package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BabyNameAccountScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    createAccountViewModel: CreateAccountViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BabyNameAccountContent(
        babyName = createAccountViewModel.babyName,
        showButton = createAccountViewModel.isBabyNameNotEmpty(),
        onBabyNameChange = createAccountViewModel::updateBabyName,
        onBabyNameDone = { focusManager.clearFocus(force = true) },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onNavigationIconClick = onNavigationIconClick,
        onNextButtonClick = onNextButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyNameAccountContent(
    babyName: String,
    showButton: Boolean,
    onBabyNameChange: (babyName: String) -> Unit,
    onBabyNameDone: () -> Unit,
    onOutsideClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_name_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (showButton) {
                FloatingActionButton(onClick = onNextButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = stringResource(id = R.string.next_label)
                    )
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            BabyNameField(
                babyName = babyName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onBabyNameChange = onBabyNameChange,
                onDone = onBabyNameDone
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabyNameAccountScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BabyNameAccountContent(
                babyName = "",
                showButton = false,
                onBabyNameChange = {},
                onBabyNameDone = {},
                onOutsideClick = {},
                onNavigationIconClick = {},
                onNextButtonClick = {}
            )
        }
    }
}