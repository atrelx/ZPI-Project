package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.models.ProductSummary

class CustomerPicker(navController: NavController)
    : BasePicker<GeneralCustomerInfo>(navController, GeneralCustomerInfo.serializer()
    ){

    fun isCustomerPickerMode() {

    }
}

