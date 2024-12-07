package com.example.amoz.ui.states
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.models.ProductSummary
import com.example.amoz.view_models.OrdersViewModel.OrderFilterParams
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal

data class OrderUIState (
    val ordersListFetched: MutableStateFlow<ResultState<List<ProductOrderSummary>>> = MutableStateFlow(ResultState.Idle),

    val ordersList: List<ProductOrderSummary> = emptyList(),
    val amountOfItems: Int = 0,
    val itemsSumPrice: BigDecimal = BigDecimal.ZERO,
    val amountOfItemsNewStatus: Int = 0,

    val productsList: List<ProductSummary> = listOf(),
    val selectedTime: Pair<Int,Int>? = null,

    val searchQuery: String = "",
    val filterParams: OrderFilterParams = OrderFilterParams(),
    val filteredSortedOrdersList: List<ProductOrderSummary> = ordersList,

    val currentAddEditOrderDetails: ProductOrderDetails? = null,
    val currentAddEditOrderState: MutableStateFlow<ResultState<ProductOrderCreateRequest>> = MutableStateFlow(ResultState.Idle),

    val ordersListIsLoading: Boolean = true, // TODO: remove this part

    val showOrdersList: Boolean = true,
    val isFilterBottomSheetExpanded: Boolean = false,
    val isAddressBottomSheetExpanded: Boolean = false,
    val isDropdownStatusExpanded: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isQuantityBottomSheetExpanded: Boolean = false,
)