package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@Composable
fun BabyBirthplaceAccountScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    createAccountViewModel: CreateAccountViewModel
) {
    val focusManager = LocalFocusManager.current

    BabyBirthplaceAccountContent(
        birthplace = createAccountViewModel.birthplace,
        showButton = createAccountViewModel.isBirthplaceNotEmpty(),
        onBirthplaceChange = createAccountViewModel::updateBirthplace,
        onBirthplaceDone = { focusManager.clearFocus(force = true) },
        onNavigationIconClick = onNavigationIconClick,
        onNextButtonClick = onNextButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBirthplaceAccountContent(
    birthplace: String,
    showButton: Boolean,
    onBirthplaceChange: (birthplace: String) -> Unit,
    onBirthplaceDone: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.birthplace_label)) },
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
            BirthplaceField(
                birthplace = birthplace,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onBirthplaceChange = onBirthplaceChange,
                onDone = onBirthplaceDone
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabyBirthplaceAccountScreenPreview() {
    BabyBirthplaceAccountContent(
        birthplace = "",
        showButton = false,
        onBirthplaceChange = {},
        onBirthplaceDone = {},
        onNavigationIconClick = {},
        onNextButtonClick = {}
    )
}