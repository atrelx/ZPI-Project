package com.example.bussiness.ui.screens.bottom_screens.company

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bussiness.R
import com.example.bussiness.app.NavigationItem
import com.example.bussiness.app.companyInfoScreenItems
import com.example.bussiness.ui.theme.AmozApplicationTheme
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CompanyScreen(
    navController: NavController,
    paddingValues: PaddingValues) {
    AmozApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CompanyBannerAndLogo(
                    banner = R.drawable.pizzeria_banner,
                    logo = R.drawable.pizzeria
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // --------------------- Company name, description ---------------------
                    Text(
                        text = stringResource(id = R.string.company_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(id = R.string.company_description),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // ------------------- Company address, workers, customers -------------------
                    
                    val itemsDescriptions = listOf(
                        stringResource(R.string.company_address_example),
                        stringResource(R.string.company_address_example),
                        stringResource(R.string.company_address_example),
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    companyInfoScreenItems.forEach() { companyInfoItem ->
                        CompanyInfoItems(
                            icon = companyInfoItem.selectedIcon,
                            title = stringResource(companyInfoItem.title),
                            itemDescription = itemsDescriptions[companyInfoScreenItems.indexOf(companyInfoItem)],
                            navigateToScreen = {
                                companyInfoItem.screen?.let { navController.navigate(it) }
                            }
                        )
                    }
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
fun CompanyInfoItems(
    icon: ImageVector,
    title: String,
    itemDescription: String,
    navigateToScreen: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { navigateToScreen() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(text = title)
        },
        supportingContent = {
            Text(text = itemDescription)
        },
        trailingContent = {
            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
        },
    )
}