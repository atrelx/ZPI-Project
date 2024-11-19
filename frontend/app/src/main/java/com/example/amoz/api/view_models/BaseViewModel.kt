package com.example.amoz.api.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class BaseViewModel: ViewModel() {
    protected val tag: String
        get() = "${this::class.java.simpleName}.${Thread.currentThread().stackTrace[3].methodName}"

    fun <T> performRepositoryAction(binding: MutableLiveData<ResultState<T>>,
                                    failureMessage: String = "Please try again later",
                                    action: suspend () -> T?,  onSuccess: ((T) -> Unit)? = null) {
        binding.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = action()
                if (response != null) {
                    Log.i(tag, response.toString())
                    binding.value = ResultState.Success(response)
                    onSuccess?.invoke(response)
                } else {
                    binding.value = ResultState.Failure(failureMessage)
                }
            } catch (e: Exception) {
                Log.e(tag, "An exeption has occured: ${e.message}", e)
                binding.value = ResultState.Failure("Please try again later")
            }
        }
    }

    fun <T> assertModelIsValid(binding: LiveData<SyncResultState<T>>, action: (T) -> Unit) {
        when (val state = binding.value) {
            is SyncResultState.Success -> {
                action(state.data)
            }
            is SyncResultState.Failure -> {
                Log.w(tag, state.message)
            }
            else ->  {
                Log.e(tag, "Assertion error")
            }
        }
    }
}