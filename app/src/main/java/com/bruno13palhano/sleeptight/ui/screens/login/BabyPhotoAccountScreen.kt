package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BabyPhotoAccountScreen(
    onNextButtonClick: () -> Unit
) {
    Column {
        Text(text = "Baby Photo Account Screen")

        Button(onClick = onNextButtonClick) {
            Text(text = "Go to Baby Name")
        }
    }
}