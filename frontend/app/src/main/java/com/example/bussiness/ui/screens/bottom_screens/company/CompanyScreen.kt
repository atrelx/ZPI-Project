package com.example.bussiness.ui.screens.bottom_screens.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bussiness.R
import com.example.bussiness.app.companyInfoScreenItems
import com.example.bussiness.ui.theme.AmozApplicationTheme

@Composable
fun CompanyScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyScreenViewModel) {
    AmozApplicationTheme {
        val companyUiState by companyViewModel.companyUiState.collectAsState()

        val clipboardManager = LocalClipboardManager.current

        val workersDescription = stringResource(R.string.company_workers_description)
        val customersDescription = stringResource(R.string.company_customers_description)

        companyViewModel.updateCompanyAddress(
            stringResource(R.string.company_address_example))

        companyViewModel.updateCompanyNameDescription(
            stringResource(id = R.string.company_name),
            stringResource(id = R.string.company_description))

        companyViewModel.updateCompanyNipRegon(
            stringResource(R.string.company_number_description),
            stringResource(R.string.company_number_additional_description))

        val itemsDescriptions = remember {
            listOf(
                companyUiState.companyAddress,
                workersDescription,
                customersDescription,
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // --------------------- Company banner, logo ---------------------
                CompanyBannerAndLogo(
                    banner = companyUiState.companyBanner,
                    logo = companyUiState.companyLogo
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // --------------------- Company name, description ---------------------
                    Text(
                        text = companyUiState.companyName,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = companyUiState.companyDescription,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // ------------------- Company address, workers, customers -------------------
                    Spacer(modifier = Modifier.height(5.dp))
                    companyInfoScreenItems.forEach { companyInfoItem ->
                        CompanyInfoItem(
                            leadingIcon = companyInfoItem.selectedIcon,
                            title = stringResource(companyInfoItem.title),
                            itemDescription = itemsDescriptions[companyInfoScreenItems.indexOf(companyInfoItem)],
                            trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward,
                            onClick = {
                                companyInfoItem.screen?.let { navController.navigate(it) }
                            }
                        )
                    }
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
    }
}

@Composable
fun CompanyBannerAndLogo(banner: Int, logo: Int) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(banner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )

        Image(
            painter = painterResource(logo),
            contentDescription = null,
            modifier = Modifier
                .size(125.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.CenterStart),
            contentScale = ContentScale.Crop
        )
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