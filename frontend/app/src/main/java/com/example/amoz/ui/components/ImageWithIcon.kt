package com.example.amoz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageWithIcon(
    onClick: () -> Unit,
    image: String = "",
    contentDescription: String? = null,
    size: Dp = 160.dp,
    shape: RoundedCornerShape = CircleShape,
    iconImage: ImageVector = Icons.Default.Edit,
    iconContentDescription: String? = null,
    isEditing: Boolean = true
) {
    Box {
        Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(size)
                .clip(shape)
                .clickable { onClick() }
        )
        if (isEditing) {
            IconButton(
                onClick = { onClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
            ) {
                Icon(imageVector = iconImage, contentDescription = iconContentDescription)
            }
        }
    }
}