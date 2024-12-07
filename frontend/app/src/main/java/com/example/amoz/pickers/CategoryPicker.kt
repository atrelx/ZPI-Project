package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.screens.Screens

class CategoryPicker(navController: NavController)
    :BasePicker<CategoryTree>(navController, CategoryTree.serializer()) {

    fun isCategoryPickerMode(): Boolean {
        return getMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE)
    }

    fun isCategoryLeavesOnlyPickerMode(): Boolean {
        return getMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE_LEAVES_ONLY)
    }

    fun getPickedCategory(): CategoryTree? {
        return getPickedItem(SavedStateHandleKeys.PICKED_CATEGORY_TREE)
    }

    fun pickCategory(categoryTree: CategoryTree) {
        pickItem(SavedStateHandleKeys.PICKED_CATEGORY_TREE, categoryTree)
        navController.popBackStack()
        setCategoryPickerMode(false)
        setCategoryLeavesOnlyPickerMode(false)
    }

    fun navigateToCategoryScreen(pickLeavesOnly: Boolean) {
        setCategoryPickerMode(true)
        if (pickLeavesOnly) { setCategoryLeavesOnlyPickerMode(true) }
        navController.navigate(Screens.Categories.route)
    }

    fun removePickedCategory() {
        removePickedItem(SavedStateHandleKeys.PICKED_CATEGORY_TREE)
    }

    private fun setCategoryPickerMode(mode: Boolean) {
        setMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE, mode)
    }

    private fun setCategoryLeavesOnlyPickerMode(mode: Boolean) {
        setMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE_LEAVES_ONLY, mode)
    }

}