package com.example.amoz.ui.screens.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.HorizontalDividerWithText
import com.example.amoz.ui.commonly_used_components.CloseOutlinedButton
import com.example.amoz.ui.commonly_used_components.PrimaryFilledButton
import com.example.amoz.ui.screens.categories.filtered_list.CategoryListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryBottomSheet(
    category: CategoryTree? = null,
    onDismissRequest: () -> Unit,
    onComplete: (String, List<String>) -> Unit,
    onSubcategoryEdit: (CategoryTree) -> Unit
) {
    var categoryName by remember { mutableStateOf(category?.name ?: "") }
    var categoryChildren = category?.childCategories?.toMutableList() ?: mutableListOf()
    val newCategoryChildren = remember { mutableStateListOf<String>() }

    LaunchedEffect(category) {
        categoryName = category?.name ?: ""
        categoryChildren = category?.childCategories?.toMutableList() ?: mutableListOf()
    }

    val isFormValid by remember {
        derivedStateOf {
            (categoryName.isNotBlank() && category?.name != categoryName) ||
                    newCategoryChildren.isNotEmpty()
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bottomSheetTitle =
        if (category == null) stringResource(R.string.add_category_title)
        else stringResource(R.string.edit_category_title)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // -------------------- Bottom sheet title --------------------
            Text(
                text = bottomSheetTitle,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            // -------------------- CategoryTree name --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text(stringResource(id = R.string.category_name)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                },
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Subcategories --------------------
            HorizontalDividerWithText(stringResource(id = R.string.subcategories))
            // -------------------- Existing subcategories --------------------
            categoryChildren.forEach { category ->
                CategoryListItem(
                    category = category,
                    isChild = false,
                    isEditable = true,
                    onEdit = onSubcategoryEdit,
                    hasChildren = false
                )
            }
            // -------------------- New subcategories --------------------
            newCategoryChildren.forEachIndexed { index, newCategoryName ->
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newCategoryName,
                    onValueChange = { updatedValue ->
                        newCategoryChildren[index] = updatedValue
                    },
                    label = { Text(stringResource(id = R.string.subcategory_name)) },
                    maxLines = 1,
                    singleLine = true
                )
            }

            OutlinedButton(
                onClick = {
                    newCategoryChildren.add("")
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.add_subcategory),
                )
            }

            // -------------------- Complete adding --------------------
            Spacer(modifier = Modifier.height(10.dp))

            PrimaryFilledButton(
                text = stringResource(id = R.string.confirm),
                onClick = {
                    onComplete(categoryName, newCategoryChildren)
                },
                enabled = isFormValid,
            )
            // -------------------- Close bottom sheet --------------------
            val scope = rememberCoroutineScope()
            CloseOutlinedButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                },
                text = stringResource(id = R.string.close)
            )
        }
    }
}