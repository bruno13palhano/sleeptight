package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BabyBirthAccountScreen(
    onCreateAccountSuccess: () -> Unit
) {
    Column {
        Text(text = "Baby Birth Account Screen")

        Button(onClick = onCreateAccountSuccess) {
            Text(text = "Go to Home")
        }
    }
}