package com.example.amoz.ui.components.filters

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
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.ui.components.pickers.CategoryPickerListItem
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.view_models.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreFiltersBottomSheet(
    navController: NavController,
    onDismissRequest: () -> Unit,
    onCancelFilters: () -> Unit,
    filterParams: ProductsViewModel.FilterParams,
    onSaveFilterParams: (ProductsViewModel.FilterParams) -> Unit,
    onApplyFilters: (ProductsViewModel.FilterParams) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var filterParamsState by remember(filterParams) { mutableStateOf(filterParams) }


    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
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
                sortingType = filterParamsState.sortingType,
                onSortingTypeChange = {
                    filterParamsState = filterParamsState.copy(sortingType = it)
                }
            )
            // -------------------- Price filter --------------------
            PriceFilter(
                priceFrom = filterParamsState.priceFrom,
                priceTo = filterParamsState.priceTo,
                onPriceFromChange = {
                    filterParamsState = filterParamsState.copy(priceFrom = it)
                },
                onPriceToChange = {
                    filterParamsState = filterParamsState.copy(priceTo = it)
                },
            )
            // -------------------- Product's category --------------------
            CategoryPickerListItem(
                category = filterParamsState.category,
                navController = navController,
                onSaveState = { onSaveFilterParams(filterParamsState) },
                onCategoryChange = { filterParamsState = filterParamsState.copy(category = it) },
                getCategoryId = { it?.categoryId },
                getCategoryName = { it?.name }
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
                    onApplyFilters(filterParamsState)
                },
                text = stringResource(id = R.string.apply_filters)
            )
        }
    }
}

