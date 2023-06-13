package com.bruno13palhano.sleeptight.ui.screens.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewNotificationScreen(
    onDoneButtonClick: () -> Unit
) {
    Column {
        Text(text = "New Notification Screen")

        Button(onClick = onDoneButtonClick) {
            Text(text = "Go to Notifications")
        }
    }
}