package com.example.amoz.ui.screens.bottom_screens.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.app.companyInfoScreenItemsMap
import com.example.amoz.data.NavItem
import com.example.amoz.ui.commonly_used_components.AddressBottomSheet
import com.example.amoz.ui.theme.AmozApplicationTheme

@Composable
fun CompanyScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyScreenViewModel,
    companyInfoScreenItems: List<NavItem> = companyInfoScreenItemsMap.values.toList()
) {
    AmozApplicationTheme {
        val navBackStackEntry = remember { navController.currentBackStackEntryFlow }
        LaunchedEffect(navBackStackEntry) {
            companyViewModel.fetchCompanyDetails()
        }

        val companyUiState by companyViewModel.companyUiState.collectAsState()
        val clipboardManager = LocalClipboardManager.current

        val workersDescription = stringResource(R.string.company_employees_description)
        val customersDescription = stringResource(R.string.company_customers_description)

        val itemsDescriptions by remember {
            derivedStateOf {
                mutableStateListOf(
                    workersDescription,
                    customersDescription,
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // --------------------- Company banner, logo ---------------------
                CompanyLogoAndName(
                    companyLogo = companyUiState.companyLogo,
                    companyName = companyUiState.companyName,
                    onClick = { companyViewModel.expandChangeCompanyNameBottomSheet(true) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --------------------- Company info ---------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    companyInfoScreenItems.zip(itemsDescriptions).forEach { (companyInfoItem, description) ->
                        CompanyInfoItem(
                            leadingIcon = companyInfoItem.icon,
                            title = stringResource(companyInfoItem.title),
                            itemDescription = description,
                            trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward,
                            onClick = {
                                navController.navigate(companyInfoItem.screenRoute)
                            }
                        )
                    }

                    // ------------------- Company address -------------------
                    CompanyInfoItem(
                        leadingIcon = Icons.Outlined.LocationOn,
                        title = stringResource(R.string.company_address_screen),
                        itemDescription = companyUiState.companyFullAddress,
                        trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward,
                        onClick = {
                            companyViewModel.expandChangeCompanyAddressBottomSheet(true)
                        }
                    )

                    // ------------------- Company Nip, Regon -------------------
                    CompanyInfoItem(
                        leadingIcon = null,
                        title = stringResource(R.string.company_number_name),
                        itemDescription = companyUiState.companyNumber,
                        trailingIcon = null,
                        onClick = { clipboardManager.setText(
                            AnnotatedString(companyUiState.companyNumber))
                        }
                    )
                    CompanyInfoItem(
                        leadingIcon = null,
                        title = stringResource(R.string.company_number_additional),
                        itemDescription = companyUiState.companyRegon,
                        trailingIcon = null,
                        onClick = { clipboardManager.setText(
                            AnnotatedString(companyUiState.companyRegon))
                        }
                    )
                }
            }
        }

        if (companyUiState.changeCompanyAddressBottomSheetExpanded) {
            AddressBottomSheet(
                bottomSheetTitle = stringResource(id = R.string.address_change_title_company),
                street = companyUiState.companyAddress?.street ?: "",
                houseNumber = companyUiState.companyAddress?.streetNumber ?: "",
                apartmentNumber = companyUiState.companyAddress?.apartmentNumber,
                city = companyUiState.companyAddress?.city ?: "",
                postalCode = companyUiState.companyAddress?.postalCode ?: "",
                additionalInfo = companyUiState.companyAddress?.additionalInformation ?: "",
                onDismissRequest = {
                    companyViewModel.expandChangeCompanyAddressBottomSheet(false)
                },
                onDone = companyViewModel::updateCompanyAddress
            )
        }

        if (companyUiState.changeCompanyNameBottomSheetExpanded) {
            ChangeCompanyNameBottomSheet(
                companyName = companyUiState.companyName,
                onDismissRequest = {
                    companyViewModel.expandChangeCompanyNameBottomSheet(false)
                },
                onDone = { newName -> companyViewModel.updateCompanyName(newName) }
            )
        }
    }
}

@Composable
fun CompanyLogoAndName(
    companyLogo: Int,
    companyName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(companyLogo),
            contentDescription = null,
            modifier = Modifier
                .size(125.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Column {
            Row (
                modifier = Modifier.clickable { onClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Outlined.ModeEdit,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.company_screen_description),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CompanyInfoItem(
    leadingIcon: ImageVector?,
    title: String,
    itemDescription: String,
    trailingIcon: ImageVector?,
    onClick: (() -> Unit)?
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                if (onClick != null) {
                    onClick()
                }
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        leadingContent = {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = null)
            }
        },
        headlineContent = {
            Text(text = title)
        },
        supportingContent = {
            Text(text = itemDescription)
        },
        trailingContent = {
            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null
                )
            }
        },
    )
}