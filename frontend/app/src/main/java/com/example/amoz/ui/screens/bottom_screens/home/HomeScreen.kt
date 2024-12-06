package com.example.amoz.ui.screens.bottom_screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.bottomNavigationBarNavItemsMap
import com.example.amoz.data.NavItem
import com.example.amoz.ui.components.MoreOrdersTextButton
//import com.example.amoz.view_models.OrdersViewModel
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.OrdersViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    navigateToScreen: (NavItem) -> Unit,
    paddingValues: PaddingValues,
    ordersViewModel: OrdersViewModel,
) {
    AmozApplicationTheme {
        Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
        ) {
            Column {
                CardsLazyRow(cardsList = ordersViewModel.createHomeCardList())
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
                MoreOrdersTextButton(
                    onClick = {
                        val ordersNavItem = bottomNavigationBarNavItemsMap[NavItemType.Orders]
                        navigateToScreen(ordersNavItem!!)
                    } )
                LastOrdersList(
                    ordersViewModel = ordersViewModel,
                    maxListItemsVisible = 10 )
            }
        }
    }
}



