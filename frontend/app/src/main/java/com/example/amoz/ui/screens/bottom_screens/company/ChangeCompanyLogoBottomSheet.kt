package com.example.amoz.ui.screens.bottom_screens.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MultipartBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeCompanyLogoBottomSheet(
    companyLogoState: MutableStateFlow<ResultState<ImageBitmap>>,
    onImageChange: (MultipartBody.Part) -> Unit,
    readOnly: Boolean = false,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var imageState by remember { mutableStateOf<MultipartBody.Part?>(null) }
            // -------------------- Title --------------------
            Text(
                text = stringResource(id = R.string.company_logo),
                style = MaterialTheme.typography.headlineMedium
            )

            ImageWithIcon(
                isEditing = readOnly,
                onClick = {}
            )

            // -------------------- Confirm button --------------------
            if(!readOnly) {
                PrimaryFilledButton(
                    onClick = {
//                    imageState?.let { onImageChange() }
                    },
                    text = stringResource(id = R.string.company_logo_change)
                )
            }

        }
    }
}