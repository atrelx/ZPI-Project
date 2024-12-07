package com.example.amoz.view_models

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.enums.Status
import com.example.amoz.api.repositories.CustomerRepository
import com.example.amoz.api.repositories.ProductOrderRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.requests.ProductOrderItemCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.screens.bottom_screens.orders.orders_list.OrderListFilter
import com.example.amoz.ui.states.OrderUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor (
    private val orderRepository: ProductOrderRepository,
    private val productRepository: ProductVariantRepository,
    private val customerRepository: CustomerRepository
): BaseViewModel() {

    private val _ordersUiState = MutableStateFlow(OrderUIState())
    val ordersUiState: StateFlow<OrderUIState> = _ordersUiState.asStateFlow()

    private val _addressCreateRequestState = MutableStateFlow(
        AddressCreateRequest()
    )

    private val orderFilter = OrderListFilter()

    // --------------------------------

    fun changeFilterBottomSheetStatus(state: Boolean) {
        _ordersUiState.update { it.copy(isFilterBottomSheetExpanded = state) }
    }

    fun changeAddressBottomSheetStatus(state: Boolean) {
        _ordersUiState.update { it.copy(isAddressBottomSheetExpanded = state) }
    }

    // --------------------------------

    private fun createOrderRequestFromVariantItems(
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


    private fun createProductOrder(orderCreateRequest: ProductOrderCreateRequest, listVariantOrderItem: List<ProductVariantOrderItem>) {
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

    fun saveCurrentAddEditOrderState(orderCreateRequest: ProductOrderCreateRequest,
                                     listVariantOrderItem: List<ProductVariantOrderItem>,
                                     currentCustomerDetails: CustomerAnyRepresentation?
    ) {
        val finalOrderRequest = createOrderRequestFromVariantItems(
            listVariantOrderItem = listVariantOrderItem,
            status = orderCreateRequest.status,
            address = orderCreateRequest.address,
            customerId = orderCreateRequest.customerId,
            trackingNumber = orderCreateRequest.trackingNumber,
            timeOfSending = orderCreateRequest.timeOfSending
        )

        _ordersUiState.update { it.copy(
            currentAddEditOrderState = MutableStateFlow(ResultState.Success(finalOrderRequest)),
            currentProductVariantDetailsList = listVariantOrderItem,
            currentCustomerDetails = currentCustomerDetails,
        ) }
    }

    fun clearCurrentAddEditOrderState() {
        _ordersUiState.update { it.copy(
            currentAddEditOrderState = MutableStateFlow(ResultState.Idle),
            currentProductVariantDetailsList = emptyList(),
            currentOrderProductVariantsImagesMap = emptyMap(),
            currentAddEditOrderDetails = null,
            isCurrentOrderNew = true,
        ) }
    }

    private fun updateProductOrder(uuid: UUID, orderCreateRequest: ProductOrderCreateRequest) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not update product order, try again",
            action = { orderRepository.updateProductOrder(uuid, orderCreateRequest) },
            onSuccess = {
                fetchOrdersList(skipLoading = true)
            }
        )
    }

    fun removeProductOrder(orderId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not remove order, try again",
            action = { orderRepository.removeProductOrder(orderId) },
            onSuccess = {
                fetchOrdersList(skipLoading = false)
            }
        )
    }

    fun generateProductOrderInvoice(orderId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not generate invoice, try again",
            action = { orderRepository.generateInvoice(orderId) }
        )
    }

    fun chooseProductOrderOperation(orderCreateRequest: ProductOrderCreateRequest, currentProductVariantDetailsList: List<ProductVariantOrderItem>) {
        if (_ordersUiState.value.currentAddEditOrderDetails == null) {
            createProductOrder(orderCreateRequest, currentProductVariantDetailsList)
        }
        else {
            val uuid = _ordersUiState.value.currentAddEditOrderDetails?.productOrderId
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
            _ordersUiState.update { it.copy(
                currentAddEditOrderDetails = null,
            ) }
            _ordersUiState.value.currentAddEditOrderState.value = ResultState.Success(ProductOrderCreateRequest())
        }
        else {
            fetchOrderDetails(orderId)
        }
    }

    private fun createProductVariantListFromOrderDetails(orderDetails: ProductOrderDetails?): List<ProductVariantOrderItem> {
        return orderDetails?.productOrderItems?.map { item ->
            ProductVariantOrderItem(
                productVariant = item.productVariant,
                quantity = item.amount,
                totalPrice = item.unitPrice * BigDecimal(item.amount)
            )
        } ?: emptyList()
    }

    private fun fetchOrderDetails(orderId: UUID) {
        performRepositoryAction(
            binding = _ordersUiState.value.currentAddEditOrderState,
            failureMessage = "Could not fetch product details, try again",
            action = {
                val orderDetails = orderRepository.getProductOrderDetails(orderId)
                orderDetails?.let {
                    Log.d("orderDetails", "$orderDetails")
                    val productOrderRequest = ProductOrderCreateRequest(orderDetails)
                    val productVariantList = createProductVariantListFromOrderDetails(orderDetails)
                    _ordersUiState.update { it.copy(
                        currentAddEditOrderDetails = orderDetails,
                        currentProductVariantDetailsList = productVariantList,
                        isCurrentOrderNew = false,
                    ) }
                    Log.d("currentProductVariantDetailsList", "$productVariantList")
                    productOrderRequest
                }
            },
            onSuccess = {
                it.customerId?.let { customerId ->
                    getCustomerDetails(customerId)
                }
            }
        )
    }

    private fun getCustomerDetails(customerId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not fetch customer details, try again",
            action = {
                val details = customerRepository.getCustomerDetails(customerId)
                if (details != null) {
                    _ordersUiState.update { it.copy(currentCustomerDetails = details) }
                }
            }
        )
    }

    fun updateSearchQuery(query: String) {
        _ordersUiState.update { currState ->
            currState.copy(
                searchQuery = query,
            )
        }
        applyFilters()
    }

    private fun applyFilters() {
        _ordersUiState.update { currState ->
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
        _ordersUiState.update { currState ->
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
        _ordersUiState.update { currState ->
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
        _ordersUiState.update { currState ->
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
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    status = null
                )
            )
        }
        applyFilters()
    }

    fun cancelFilterParams() {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = OrderFilterParams()
            )
        }
        applyFilters()
    }

    fun saveFilterParams(filterParams: OrderFilterParams) {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = filterParams
            )
        }
        applyFilters()
    }

    fun fetchOrdersList(skipLoading: Boolean = false) {
        performRepositoryAction(
            binding = _ordersUiState.value.ordersListFetched,
            failureMessage = "Could not fetch products, try again",
            skipLoading = skipLoading,
            action = { orderRepository.getAllProductOrders() },
            onSuccess = { ordersList ->
                _ordersUiState.update { it.copy(ordersList = ordersList) }
                calculateOrderTotals()
                applyFilters()
            }
        )
    }

    fun fetchOrdersListOnScreenLoad() {
            if (_ordersUiState.value.ordersListFetched.value is ResultState.Idle) {
                fetchOrdersList()
            }
        }

    private fun calculateOrderTotals() {
        var totalAmount = 0
        var totalAmountNew = 0
        var totalPrice = BigDecimal.ZERO

        _ordersUiState.value.ordersList.forEach { order ->
            totalAmount += order.sampleProductOrderItem.amount
            totalPrice += order.totalDue
            if (order.status == Status.NEW) {
                totalAmountNew += order.sampleProductOrderItem.amount
            }
        }

        _ordersUiState.update { it.copy(
            amountOfItems = totalAmount,
            itemsSumPrice = totalPrice,
            amountOfItemsNewStatus = totalAmountNew,
        ) }
    }

    fun addProductVariantToList(productVariantList: List<ProductVariantOrderItem>, productVariantOrderItem: ProductVariantOrderItem): List<ProductVariantOrderItem> {
        val updatedProductVariantList = productVariantList.toMutableList()
        if (updatedProductVariantList.none { it.productVariant.productVariantId == productVariantOrderItem.productVariant.productVariantId }) {
            updatedProductVariantList.add(productVariantOrderItem)
        }
        return updatedProductVariantList
    }

    fun removeProductVariantFromList(productVariantList: List<ProductVariantOrderItem>, productVariantOrderItem: ProductVariantOrderItem): List<ProductVariantOrderItem> {
        val updatedProductVariantList = productVariantList.toMutableList()
        updatedProductVariantList.remove(productVariantOrderItem)
        return updatedProductVariantList
    }

    fun loadProductVariantImage(productVariantId: UUID) {
        viewModelScope.launch {
            _ordersUiState.update { currentState ->
                currentState.copy(
                    productVariantsImagesMap = currentState.productVariantsImagesMap + (productVariantId to null)
                )
            }

            val image = productRepository.getProductVariantPicture(productVariantId)
            _ordersUiState.update { currentState ->
                currentState.copy(
                    productVariantsImagesMap = currentState.productVariantsImagesMap + (productVariantId to image)
                )
            }
        }
    }

    fun loadCurrentOrderProductVariantImage(productVariantId: UUID) {
        viewModelScope.launch {
            _ordersUiState.update { currentState ->
                currentState.copy(
                    currentOrderProductVariantsImagesMap = currentState.currentOrderProductVariantsImagesMap + (productVariantId to null)
                )
            }

            val image = productRepository.getProductVariantPicture(productVariantId)
            _ordersUiState.update { currentState ->
                currentState.copy(
                    currentOrderProductVariantsImagesMap = currentState.currentOrderProductVariantsImagesMap + (productVariantId to image)
                )
            }
        }
    }

    fun calculateTotalPrice(productVariantList: List<ProductVariantOrderItem>): BigDecimal {
        return productVariantList.fold(BigDecimal.ZERO) { acc, item ->
            acc + item.totalPrice
        }
    }

    fun createHomeCardList(orderUIState: OrderUIState): List<HomeCardItem> {
        return listOf(
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = "Revenues",
                cardTitleIcon = Icons.Filled.Addchart,
                valueDescription = "Total revenues:",
                value = orderUIState.itemsSumPrice.toString()
            ),
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = "Sells",
                cardTitleIcon = Icons.Filled.BarChart,
                valueDescription = "Total sells:",
                value = orderUIState.amountOfItems.toString()
            ),
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = "New orders",
                cardTitleIcon = Icons.Filled.StackedLineChart,
                valueDescription = "Total new orders:",
                value = orderUIState.amountOfItemsNewStatus.toString()
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