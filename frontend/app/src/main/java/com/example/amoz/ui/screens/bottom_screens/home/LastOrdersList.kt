package com.example.amoz.ui.screens.bottom_screens.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.firebase.SoldProduct
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LastOrdersLazyList(salesList: List<SoldProduct>, maxListItemsVisible: Int) {
    LazyColumn {
        items(salesList) { sale ->
            Log.d("ASD", "ProductItem_${sale.id}")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .combinedClickable(
                        onClick = { },

                        )
                    .testTag("ProductItem_${sale.id}")
            ) {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    headlineContent = { Text(text = sale.name, fontSize = 18.sp) },
                    leadingContent = {
                        if (sale.imageUrl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(sale.imageUrl),
                                contentDescription = "Sold Product Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    },
                    trailingContent = {
                        val totalCharacteristicsPrice = sale.characteristics.values
                            .map { it.toString().toDoubleOrNull() ?: 0.0 }
                            .sum()

                        Text(
                            text = "${sale.totalPrice + totalCharacteristicsPrice} PLN",
                            fontSize = 14.sp
                        )
                    },
                    supportingContent = {
                        Text(
                            text = SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(sale.saleDate), fontSize = 12.sp
                        )
                    }
                )
            }
        }
    }
}