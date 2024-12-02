package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.data.SavedStateHandleKeys
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class BasePicker<T> (
    protected val navController: NavController,
    private val serializer: KSerializer<T>
) {
    protected val currentSavedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    protected val previousSavedStateHandle = navController.previousBackStackEntry?.savedStateHandle

    protected fun encodeToJson(data: T): String {
        return Json.encodeToString(serializer, data)
    }

    protected fun decodeFromJson(json: String): T {
        return Json.decodeFromString(serializer, json)
    }

    protected fun setNavElementsVisibleMode(mode: Boolean) {
        setMode(SavedStateHandleKeys.SHOW_NAV_ELEMENTS, mode)
    }

    protected fun setMode(key: String, mode: Boolean) {
        currentSavedStateHandle?.set(key, mode)
    }
}