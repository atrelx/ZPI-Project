package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.data.ProductVariant
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductVariantBottomSheet(
    onDismissRequest: () -> Unit,
    onComplete: (ProductVariant) -> Unit,
    productVariant: ProductVariant,
) {
    var productName by remember { mutableStateOf(productVariant.name) }
    var productBarcode by remember { mutableStateOf(productVariant.barcode) }
    var productImpactOnPrice by remember { mutableStateOf(productVariant.impactOnPrice) }
    var productImage by remember { mutableStateOf(productVariant.image) }
    var productAttributes by remember { mutableStateOf(productVariant.attributes.toList()) }

    val isFormValid by remember {
        derivedStateOf {
            productName.isNotBlank() && productImpactOnPrice.toString().isNotBlank()
        }
    }

    val scope = rememberCoroutineScope()
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            }
        )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // -------------------- Bottom sheet title --------------------
            Text(
                text = stringResource(R.string.products_add_product_variant),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            // -------------------- Product image --------------------
            ImageWithIcon(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp)
            )

            // -------------------- Product basic info --------------------
            ProductNameDescriptionPrice(
                productName = productName,
                productPrice = productImpactOnPrice,
                onNameChange = { productName = it },
                onPriceChange = { productImpactOnPrice = it },
            )
            // -------------------- Product barcode --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = productBarcode.toString(),
                onValueChange = { newBarcode ->
                    newBarcode.toIntOrNull()?.let {
                        productBarcode = it
                    }
                },
                label = { Text(text = stringResource(R.string.product_barcode)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                singleLine = true
            )
            // -------------------- Product barcode --------------------
            AttributesList(
                productAttributes = productAttributes,
                onAttributesChange = { productAttributes = it }
            )

            // -------------------- Complete adding --------------------
            Spacer(modifier = Modifier.height(15.dp))

            PrimaryFilledButton(
                onClick = {
                    onComplete(
                        productVariant.copy(
                            name = productName,
                            impactOnPrice = productImpactOnPrice,
                            barcode = productBarcode,
                            image = productImage,
                            attributes = productAttributes.toMap()
                        )
                    )
                    onDismissRequest()
                },
                text = stringResource(id = R.string.done),
                enabled = isFormValid
            )
            // -------------------- Close bottom sheet --------------------
            CloseOutlinedButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                },
                text =  stringResource(R.string.close)
            )
        }
    }
}