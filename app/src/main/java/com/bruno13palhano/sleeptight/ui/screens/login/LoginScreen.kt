package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCreateAccountButtonClick: () -> Unit
) {
    Column {
        Text(text = "Login Screen")

        Button(onClick = onLoginSuccess) {
            Text(text = "Go to Home")
        }

        Button(onClick = onCreateAccountButtonClick) {
            Text(text = "Go to Create Account")
        }
    }
}