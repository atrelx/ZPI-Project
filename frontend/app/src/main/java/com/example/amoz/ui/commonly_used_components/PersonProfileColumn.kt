package com.example.amoz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.enums.Sex
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PersonProfileColumn(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .border(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp)
        ),
    personPhoto: Int?,
    personFirstName: String,
    personLastName: String,
    personEmail: String?,
    personPhoneNumber: String,
    personSex: Sex,
    personBirthDate: LocalDate
) {

    val listItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
    val clipboardManager = LocalClipboardManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // -------------------- Full name, profile image --------------------
        ListItem(
            modifier = modifier.then(Modifier
                .clickable {
                    clipboardManager.setText(
                        AnnotatedString("$personFirstName $personLastName")
                    )
                }),
            leadingContent = {
                personPhoto?.let {
                    Image(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        painter = painterResource(id = personPhoto),
                        contentDescription = null
                    )
                } ?: run {
                    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null)
                }
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_full_name)) },
            headlineContent = {
                Text(
                    text = "$personFirstName $personLastName",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = listItemColors
        )

        // -------------------- Email --------------------
        ListItem(
            modifier = modifier.then(Modifier
                .clickable {
                    personEmail?.let {
                        clipboardManager.setText(
                            AnnotatedString(it)
                        )
                    }
                }),
            leadingContent = {
                Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_email)) },
            headlineContent = { Text(text = personEmail ?: "No e-mail") },
            colors = listItemColors
        )

        // -------------------- Phone number --------------------
        personPhoneNumber.let { phone ->
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(phone)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(id = R.string.profile_phone_number)) },
                headlineContent = { Text(text = phone) },
                colors = listItemColors
            )
        }

        // -------------------- Sex --------------------
        ListItem(
            modifier = modifier,
            leadingContent = {
                Icon(imageVector = Icons.Outlined.Man, contentDescription = null)
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_sex)) },
            headlineContent = { Text(text = personSex.getName()) },
            colors = listItemColors
        )

        // -------------------- Birth date --------------------
        ListItem(
            modifier = modifier.then(Modifier
                .clickable {
                    clipboardManager.setText(
                        AnnotatedString(
                            personBirthDate.format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            )
                        )
                    )
                }),
            leadingContent = {
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_birth_date)) },
            headlineContent = {
                Text(
                    text = personBirthDate.format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    )
                )
            },
            colors = listItemColors
        )
    }
}