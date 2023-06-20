package com.bruno13palhano.sleeptight.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.bruno13palhano.sleeptight.R

@Composable
fun CommonMenu(
    expanded: Boolean,
    onDismissRequest: (expanded: Boolean) -> Unit,
    onClick: (index: Int) -> Unit
) {
    val commonMenuItems = arrayOf(
        stringResource(id = R.string.delete_nap_label),
        stringResource(id = R.string.share_label)
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) }
    ) {
        commonMenuItems.forEachIndexed { index, itemValue ->
            DropdownMenuItem(
                text = { Text(text = itemValue) },
                onClick = {
                    onDismissRequest(false)
                    onClick(index)
                }
            )
        }
    }
}