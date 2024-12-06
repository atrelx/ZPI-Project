package com.example.amoz.view_models

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.amoz.api.enums.Status
import com.example.amoz.api.repositories.ProductOrderRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.requests.ProductOrderItemCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.screens.bottom_screens.orders.orders_list.OrderListFilter
import com.example.amoz.ui.states.OrderUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor (
    private val orderRepository: ProductOrderRepository
): BaseViewModel() {

    private val _orderUiState = MutableStateFlow(OrderUIState())
    val orderUiState: StateFlow<OrderUIState> = _orderUiState.asStateFlow()

    private val _addressCreateRequestState = MutableStateFlow(
        AddressCreateRequest()
    )

    private val _productOrderItemsState = MutableStateFlow(
        mutableListOf<ProductOrderItemCreateRequest>()
    )

    private val orderFilter = OrderListFilter()

    var currentProductVariantDetailsList = emptyList<ProductVariantOrderItem>()
    val currentPickedVariant: ProductVariantDetails? = null

    init {
        fetchOrdersList()
    }

    // --------------------------------

    fun addProduct(product: ProductSummary) {
        _orderUiState.update {
            it.copy(
                productsList = it.productsList + product
            )
        }
    }

    fun changeFilterBottomSheetStatus(state: Boolean) {
        _orderUiState.update { it.copy(isFilterBottomSheetExpanded = state) }
    }

    fun changeAddressBottomSheetStatus(state: Boolean) {
        _orderUiState.update { it.copy(isAddressBottomSheetExpanded = state) }
    }

    fun changeDropdownStatus(state: Boolean) {
        _orderUiState.update { it.copy(isDropdownStatusExpanded = state) }
    }

    fun changeDatePicker(state: Boolean) {
        _orderUiState.update {
            it.copy(isDatePickerVisible = state)
        }
    }

    fun changeTimePicker(state: Boolean) {
        _orderUiState.update { it.copy(isTimePickerVisible = state) }
    }

    fun changeQuantityBottomSheet(state: Boolean) {
        _orderUiState.update { it.copy(isQuantityBottomSheetExpanded = state) }
    }

    // --------------------------------

    fun createOrderRequestFromVariantItems(
        listVariantOrderItem: List<ProductVariantOrderItem>,
        status: Status = Status.NEW,
        address: AddressCreateRequest? = null,
        customerId: UUID? = null,
        trackingNumber: String? = null,
        timeOfSending: LocalDateTime? = null
    ): ProductOrderCreateRequest {
        val productOrderItems = listVariantOrderItem.map { variantOrderItem ->
            ProductOrderItemCreateRequest(
                productVariantId = variantOrderItem.productVariant.productVariantId,
                amount = variantOrderItem.quantity
            )
        }

        return ProductOrderCreateRequest(
            status = status,
            productOrderItems = productOrderItems,
            address = address,
            customerId = customerId,
            trackingNumber = trackingNumber,
            timeOfSending = timeOfSending
        )
    }


    fun createProductOrder(orderCreateRequest: ProductOrderCreateRequest, listVariantOrderItem: List<ProductVariantOrderItem>) {
        val finalOrderRequest = createOrderRequestFromVariantItems(
            listVariantOrderItem = listVariantOrderItem,
            status = orderCreateRequest.status,
            address = orderCreateRequest.address,
            customerId = orderCreateRequest.customerId,
            trackingNumber = orderCreateRequest.trackingNumber,
            timeOfSending = orderCreateRequest.timeOfSending
        )

        performRepositoryAction(
            binding = null,
            failureMessage = "Could not create order, try again",
            action = { orderRepository.createProductOrder(finalOrderRequest) },
            onSuccess = {
                fetchOrdersList(skipLoading = true)
            }
        )
    }

    fun updateProductOrder(uuid: UUID, orderCreateRequest: ProductOrderCreateRequest) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not update product order, try again",
            action = { orderRepository.updateProductOrder(uuid, orderCreateRequest) },
            onSuccess = {
                fetchOrdersList(skipLoading = true)
            }
        )
    }

    fun chooseProductOrderOperation(orderCreateRequest: ProductOrderCreateRequest, currentProductVariantDetailsList: List<ProductVariantOrderItem>) {
        if (_orderUiState.value.currentAddEditOrderDetails == null) {
            createProductOrder(orderCreateRequest, currentProductVariantDetailsList)
        }
        else {
            val uuid = _orderUiState.value.currentAddEditOrderDetails?.productOrderId
            updateProductOrder(uuid!!, orderCreateRequest)
        }
    }



    fun updateAddress(request: AddressCreateRequest?) {
        request?.let {
            _addressCreateRequestState.update { it }
        }
    }

    fun updateCurrentAddEditOrder(orderId: UUID?) {
        if (orderId == null) {
            _orderUiState.update { it.copy(
                currentAddEditOrderDetails = null,
            ) }
            _orderUiState.value.currentAddEditOrderState.value = ResultState.Success(ProductOrderCreateRequest())
            Log.d("OrdersViewModelNullOrder", "State updated to: ${_orderUiState.value.currentAddEditOrderState.value}")
        }
        else {
            fetchOrderDetails(orderId)
        }
    }

    fun createProductVariantListFromOrderDetails(orderDetails: ProductOrderDetails?): List<ProductVariantOrderItem> {
        return orderDetails?.productOrderItems?.map { item ->
            ProductVariantOrderItem(
                productVariant = item.productVariant,
                quantity = item.amount,
                totalPrice = item.unitPrice * BigDecimal(item.amount)
            )
        } ?: emptyList()
    }

    fun fetchOrderDetails(orderId: UUID) {
//        val productOrderDetailsMock = ProductOrderDetails(
//            productOrderId = orderId,
//            productOrderItems = listOf(),
//            address = null,
//            customer = null,
//            trackingNumber = null,
//            timeOfSending = null,
//            timeOfCreation = LocalDateTime.now(),
//            status = Status.NEW,
//            totalDue = BigDecimal.ZERO
//        )
//        val productOrderRequestMock = ProductOrderCreateRequest(productOrderDetailsMock)
//
//        _orderUiState.update { it.copy(
//            currentAddEditOrderDetails = productOrderDetailsMock,
//        ) }
//        _orderUiState.value.currentAddEditOrderState.value = ResultState.Success(productOrderRequestMock)
//        currentProductVariantDetailsList = createProductVariantListFromOrderDetails(productOrderDetailsMock)

        performRepositoryAction(
            binding = _orderUiState.value.currentAddEditOrderState,
            failureMessage = "Could not fetch product details, try again",
            action = {
                val orderDetails = orderRepository.getProductOrderDetails(orderId)
                orderDetails?.let {
                    val productOrderRequest = ProductOrderCreateRequest(orderDetails)
                    _orderUiState.update { it.copy(currentAddEditOrderDetails = orderDetails) }
//                    _orderCreateRequestState.update { productOrderRequest }

                    currentProductVariantDetailsList = createProductVariantListFromOrderDetails(orderDetails)
                    productOrderRequest
                }
            },
        )
    }

    fun updateSearchQuery(query: String) {
        _orderUiState.update { currState ->
            currState.copy(
                searchQuery = query,
            )
        }
        applyFilters()
    }

    private fun applyFilters() {
        _orderUiState.update { currState ->
            currState.copy(
                filteredSortedOrdersList = if (currState.showOrdersList) {
                    orderFilter.filterOrders(
                        templates = currState.ordersList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedOrdersList,
            )
        }
    }

    fun clearPriceFilter(isFrom: Boolean) {
        _orderUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    priceFrom = if (isFrom) null else currState.filterParams.priceFrom,
                    priceTo = if (!isFrom) null else currState.filterParams.priceTo
                )
            )
        }
        applyFilters()
    }

    fun clearTimeOfSendingFilter(isFrom: Boolean) {
        _orderUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    timeOfSendingFrom = if (isFrom) null else currState.filterParams.timeOfSendingFrom,
                    timeOfSendingTo = if (!isFrom) null else currState.filterParams.timeOfSendingTo
                )
            )
        }
        applyFilters()
    }

    fun clearTimeOfCreationFilter(isFrom: Boolean) {
        _orderUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    timeOfCreationFrom = if (isFrom) null else currState.filterParams.timeOfCreationFrom,
                    timeOfCreationTo = if (!isFrom) null else currState.filterParams.timeOfCreationTo
                )
            )
        }
        applyFilters()
    }

    fun clearStatusFilter() {
        _orderUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    status = null
                )
            )
        }
        applyFilters()
    }

    fun cancelFilterParams() {
        _orderUiState.update { currState ->
            currState.copy(
                filterParams = OrderFilterParams()
            )
        }
        applyFilters()
    }

    fun saveFilterParams(filterParams: OrderFilterParams) {
        _orderUiState.update { currState ->
            currState.copy(
                filterParams = filterParams
            )
        }
        applyFilters()
    }

    // --------------------------------

    fun fetchOrdersList(skipLoading: Boolean = false) {
//        val ordersList = mockProductOrderSummaries()
//        _orderUiState.update { it.copy(ordersList = ordersList) }
//        _orderUiState.update { it.copy(ordersListFetched = MutableStateFlow(ResultState.Success(ordersList))) }
//        applyFilters()

        performRepositoryAction(
            binding = _orderUiState.value.ordersListFetched,
            failureMessage = "Could not fetch products, try again",
            skipLoading = skipLoading,
            action = { orderRepository.getAllProductOrders() },
            onSuccess = { ordersList ->
                _orderUiState.update { it.copy(ordersList = ordersList) }
                calculateOrderTotals()
                applyFilters()
            }
        )
    }

    fun calculateOrderTotals() {
        var totalAmount = 0
        var totalAmountNew = 0
        var totalPrice = BigDecimal.ZERO

        _orderUiState.value.ordersList.forEach { order ->
            totalAmount += order.sampleProductOrderItem.amount
            totalPrice += order.totalDue
            if (order.status == Status.NEW) {
                totalAmountNew += order.sampleProductOrderItem.amount
            }
        }

        _orderUiState.update { it.copy(
            amountOfItems = totalAmount,
            itemsSumPrice = totalPrice,
            amountOfItemsNewStatus = totalAmountNew,
        ) }
    }

    fun createHomeCardList(): List<HomeCardItem> {
        return listOf(
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = "Revenues",
                cardTitleIcon = Icons.Filled.Addchart,
                valueDescription = "Total revenues:",
                value = _orderUiState.value.itemsSumPrice.toString()
            ),
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = "Sells",
                cardTitleIcon = Icons.Filled.BarChart,
                valueDescription = "Total sells:",
                value = _orderUiState.value.amountOfItems.toString()
            ),
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = "New orders",
                cardTitleIcon = Icons.Filled.StackedLineChart,
                valueDescription = "Total new orders:",
                value = _orderUiState.value.amountOfItemsNewStatus.toString()
            )
        )
    }

    enum class SortingType {
        NONE,
        ASCENDING_PRICE, DESCENDING_PRICE,
        ASCENDING_TIME_OF_SENDING, DESCENDING_TIME_OF_SENDING,
        ASCENDING_TIME_OF_CREATION, DESCENDING_TIME_OF_CREATION
    }

    data class OrderFilterParams(
        val sortingType: SortingType = SortingType.NONE,
        val status: Status? = null,
        val priceFrom: BigDecimal? = null,
        val priceTo: BigDecimal? = null,
        val timeOfSendingFrom: LocalDateTime? = null,
        val timeOfSendingTo: LocalDateTime? = null,
        val timeOfCreationFrom: LocalDateTime? = null,
        val timeOfCreationTo: LocalDateTime? = null
    )

    data class HomeCardItem(
        val backgroundImageResource: Int,
        val cardTitle: String,
        val cardTitleIcon: ImageVector,
        val valueDescription: String,
        val value: String
    )

    data class ProductVariantOrderItem(
        val productVariant: ProductVariantDetails,
        val quantity: Int,
        val totalPrice: BigDecimal
    )
}