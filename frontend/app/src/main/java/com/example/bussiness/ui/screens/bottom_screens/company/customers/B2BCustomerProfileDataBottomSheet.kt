package com.example.bussiness.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.bussiness.R
import com.example.bussiness.data.B2BCustomer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun B2BCustomerProfileDataBottomSheet(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .border(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp)
        ),
    onDismissRequest: () -> Unit,
    b2BCustomer: B2BCustomer
) {
    val listItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
    val clipboardManager = LocalClipboardManager.current

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // -------------------- Company name --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.companyName)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Business, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(R.string.company_name)) },
                headlineContent = { Text(text = b2BCustomer.companyName) },
                colors = listItemColors
            )

            // -------------------- Email --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.email)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(id = R.string.profile_email)) },
                headlineContent = { Text(text = b2BCustomer.email) },
                colors = listItemColors
            )

            // -------------------- Company address --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.companyAddress)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Place, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(R.string.company_address)) },
                headlineContent = { Text(text = b2BCustomer.companyAddress) },
                colors = listItemColors
            )

            // -------------------- Company address --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.companyIdentifier)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Numbers, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(R.string.company_number)) },
                headlineContent = { Text(text = b2BCustomer.companyIdentifier) },
                colors = listItemColors
            )
        }
    }
}