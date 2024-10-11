package com.example.bussiness.screens.drawer_screens.expanses

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bussiness.ui.theme.BussinessTheme

@Composable
fun ExpansesTemplatesScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    expansesViewModel: ExpansesTemplatesViewModel = viewModel() ) {
        BussinessTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val expansesUiState by expansesViewModel.expansesTemplateUiState.collectAsState()
            }
        }
    }