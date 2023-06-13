package com.bruno13palhano.sleeptight.ui.screens.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NotificationsScreen(
    onItemClick: (notificationId: Long) -> Unit,
    onAddButtonClick: () -> Unit
) {
    Column {
        Text(text = "Notifications Screen")

        Button(onClick = { onItemClick(12L) }) {
            Text(text = "Go to Notification")
        }

        Button(onClick = onAddButtonClick) {
            Text(text = "Go to New Notification")
        }
    }
}