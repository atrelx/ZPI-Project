import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.models.ProductOrderSummary
import java.util.UUID

@Composable
fun OrderListItem(
    order: ProductOrderSummary,
    onOrderEdit: (UUID) -> Unit,
    currency: String,
//    positionalThreshold: Float = .45f
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = {onOrderEdit(order.productOrderId)},
            ),
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = null
            )
            order.sampleProductOrderItem?.productVariant.let { // TODO currently doesn't have image, change logic or model
//                            Image(
//                                painter = rememberAsyncImagePainter(productTemplate.imageUrl),
//                                contentDescription = "Product Image",
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .clip(RoundedCornerShape(10.dp))
//                            )
            }
        },
        headlineContent = {
            Text(
                text = order.sampleProductOrderItem?.productVariant?.variantName ?: "Empty order",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            order.status.getName().let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(color = order.status.color)
                }
            }

        },
        trailingContent = {
            Text(
                text = "${order.totalDue} $currency"
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    )
}