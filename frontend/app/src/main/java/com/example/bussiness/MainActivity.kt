
package com.example.bussiness

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.bussiness.navigation.NavigationSheet
import com.example.bussiness.ui.theme.BussinessTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BussinessTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavigationSheet()
                }
            }
        }
    }
}


