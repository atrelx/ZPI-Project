package com.example.amoz.api.sealed

sealed class SyncResultState<out T> {
    data class Success<out T>(val data: T) : SyncResultState<T>()

    data class Failure(val message: String) : SyncResultState<Nothing>()
}
