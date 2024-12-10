package com.example.amoz.ui.screens.bottom_screens.additional_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.ui.theme.AmozApplicationTheme

@Composable
fun AboutScreen(navController: NavController, paddingValues: PaddingValues) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            Column {
                Text(
                    stringResource(R.string.about_app),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}