package com.example.amoz.interfaces

import android.util.Log
import com.example.amoz.api.requests.CompanyCreateRequest
import kotlin.reflect.full.memberProperties
import javax.validation.ValidationException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

abstract class ValidatableRequest<T : ValidatableRequest<T>> {

    fun validate(): String? {
        this::class.memberProperties.forEach { property ->
            property.isAccessible = true

            @Suppress("UNCHECKED_CAST")
            val value = (property as? KProperty1<Any, Any>)?.get(this)

            when (value) {
                is ValidatableRequest<*> -> {
                    value.validate()
                }
                is List<*> -> {
                    value.filterIsInstance<ValidatableRequest<*>>()
                        .forEach { it.validate() }
                }
            }
        }
        Log.i("ValidationLogger", "Validating class: ${this::class.simpleName}")
        val validationErrorMessage: String? = null

        return validationErrorMessage
    }
}
