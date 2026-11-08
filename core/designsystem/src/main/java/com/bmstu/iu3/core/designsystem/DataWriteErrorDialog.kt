package com.bmstu.iu3.core.designsystem

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun DataWriteErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.ds_write_error_title)) },
        text = { Text(stringResource(R.string.ds_write_error_message)) },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.ds_gallery_close)) } },
    )
}
