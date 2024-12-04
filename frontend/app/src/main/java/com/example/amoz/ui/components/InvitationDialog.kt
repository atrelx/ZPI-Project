package com.example.amoz.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.amoz.R

@Composable
fun InvitationDialog(
   isAccepted: (Boolean) -> Unit,
   onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = stringResource(id = R.string.company_invitation_window_title))
        },
        text = {
            Text(text = stringResource(id = R.string.company_invitation_window_text))
        },
        confirmButton = {
            TextButton(onClick = { isAccepted(true) }) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = { isAccepted(false) }) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}
