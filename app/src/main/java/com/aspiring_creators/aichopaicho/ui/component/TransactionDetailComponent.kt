package com.aspiring_creators.aichopaicho.ui.component

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale





@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun TransactionDetailsCard(
    record: Record,
    contact: Contact?,
    type: Type?,
    isEditing: Boolean,
    onAmountChange: (Int) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onCompletionToggle: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var amountText by remember(record.amount) { mutableStateOf(record.amount.toString()) }
    var descriptionText by remember(record.description) { mutableStateOf(record.description ?: "") }

    val context = LocalContext.current

    val cardColors = if (record.typeId == TypeConstants.LENT_ID) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Red.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.3f)
        )
    }

    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = cardColors
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = record.isComplete,
                        onCheckedChange = { onCompletionToggle() }
                    )
                    Text("Completed")
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Contact Name
            ContactDisplayRow(contact)

            // Type
            DetailRow(
                label = "Type",
                value = type?.name ?: "Unknown Type",
                isEditing = false
            )

            // Amount
            if (isEditing) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        it.toIntOrNull()?.let { amount -> onAmountChange(amount) }
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("NPR.") }
                )
            } else {
                DetailRow(
                    label = "Amount",
                    value = "NPR.${record.amount}",
                    isEditing = false
                )
            }

            // Date
            DetailRow(
                label = "Date",
                value = dateFormatter.format(Date(record.date)),
                isEditing = false
            )

            // Description
            if (isEditing) {
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = {
                        descriptionText = it
                        onDescriptionChange(it)
                    },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            } else {
                DetailRow(
                    label = "Description",
                    value = record.description ?: "No description",
                    isEditing = false
                )
            }

            // Created/Updated info
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Created: ${dateFormatter.format(Date(record.createdAt))}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Updated: ${dateFormatter.format(Date(record.updatedAt))}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isEditing: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ContactDisplayRow(
    contact: Contact?,
    ) {
    val context = LocalContext.current
    val validContactId = contact?.contactId?.toLongOrNull() // Safely convert to Long

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween, // Or Arrangement.SpaceEvenly
        verticalAlignment = Alignment.CenterVertically
    ) {

        DetailRow(
            label = "Contact",
            value = contact?.name ?: "Unknown Contact",
            isEditing = false,
        )

        Button(
            onClick = {
                validContactId?.let { id -> // Only execute if id is not null
                    openContactDetails(context = context, contactId = id)
                }
            },
            enabled = validContactId != null // Button is enabled only if there's a valid ID
        ) {
            Text(text = "View")
        }
    }
}
