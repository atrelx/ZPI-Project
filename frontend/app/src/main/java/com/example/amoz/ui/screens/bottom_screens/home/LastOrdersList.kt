package com.example.amoz.ui.screens.bottom_screens.home

import OrderListItem
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.AppPreferences
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.view_models.OrdersViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun LastOrdersList(
    ordersViewModel: OrdersViewModel,
    maxListItemsVisible: Int
) {
    val orderListUiState by ordersViewModel.orderUiState.collectAsState()
    val stateView: MutableStateFlow<ResultState<List<Any>>> = orderListUiState.ordersListFetched as MutableStateFlow<ResultState<List<Any>>>
    val currContext = LocalContext.current
    val appPreferences = remember { AppPreferences(currContext) }
    val currency by appPreferences.currency.collectAsState(initial = "USD")

    ResultStateView(
        state = stateView,
        onPullToRefresh = {
            ordersViewModel.fetchOrdersList(skipLoading = true)
        }
    ) {
        val sortedOrdersList = orderListUiState.ordersList
            .sortedByDescending { it.timeOfCreation }
            .take(maxListItemsVisible)

        LazyColumn (
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
            items(
                sortedOrdersList,
                key = {it.productOrderId}
            ) { order ->
                OrderListItem(
                    order = order,
                    onOrderEdit = {},
                    currency = currency!!
                )
            }
        }
    }
}