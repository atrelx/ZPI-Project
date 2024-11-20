package com.example.amoz.ui.screens.bottom_screens.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.filters.ListSorting
import com.example.amoz.ui.components.filters.PriceFilter
import com.example.amoz.view_models.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreFiltersBottomSheet(
    onDismissRequest: () -> Unit,
    onCancelFilters: () -> Unit,
    onApplyFilters: (ProductsViewModel.FilterParams) -> Unit,
    priceFrom: Double,
    priceTo: Double,
    sortingType: ProductsViewModel.SortingType,
    category: String,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var sortingTypeState by remember { mutableStateOf(sortingType) }

    var priceFromState by remember {
        mutableStateOf(
            if (priceFrom == 0.0) { "" } else { priceFrom.toString() }
        )
    }
    var priceToState by remember {
        mutableStateOf(
            if (priceTo == Int.MAX_VALUE.toDouble()) { "" } else { priceTo.toString() }
        )
    }

    var categoryState by remember { mutableStateOf(category) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -------------------- Sorting --------------------
            ListSorting(
                sortingType = sortingTypeState,
                onSortingTypeChange = { sortingTypeState = it }
            )
            // -------------------- Price filter --------------------
            PriceFilter(
                priceFrom = priceFromState,
                priceTo = priceToState,
                onPriceFromChange = { priceFromState = it },
                onPriceToChange = { priceToState = it },
            )
            // -------------------- Product's category --------------------
            CategoryPicker(
                category = category,
                onCategoryChange = { categoryState = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // -------------------- Cancel and apply --------------------
            CloseOutlinedButton(
                onClick = {
                    onCancelFilters()
                    onDismissRequest()
                },
                text = stringResource(id = R.string.clear_filters)
            )
            PrimaryFilledButton(
                onClick = {
                    onDismissRequest()
                    onApplyFilters(
                        ProductsViewModel.FilterParams(
                            sortingTypeState,
                            priceFromState.toDoubleOrNull()?:0.0,
                            priceToState.toDoubleOrNull()?:Int.MAX_VALUE.toDouble(),
                            categoryState
                        )
                    )
                },
                text = stringResource(id = R.string.apply_filters)
            )
        }
    }
}

