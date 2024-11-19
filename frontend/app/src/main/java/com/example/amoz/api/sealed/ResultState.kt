package com.example.amoz.api.sealed

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()

    data class Failure(val message: String) : ResultState<Nothing>()

    data object Loading : ResultState<Nothing>()
}
