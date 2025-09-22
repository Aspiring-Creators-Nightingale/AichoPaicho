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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
// import androidx.compose.material3.CardColors // Not needed directly
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
// import androidx.compose.material3.DividerDefaults // Not used directly
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspiring_creators.aichopaicho.CurrencyUtils
import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.Type
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme
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
    onAmountChange: (String) -> Unit, // Changed to String
    onDescriptionChange: (String) -> Unit,
    onDateChange: (Long) -> Unit, // Kept, though no UI for edit in this card
    onCompletionToggle: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    // Update local state handling for editing
    var amountText by remember(record.amount, isEditing) {
        // Initialize with formatted string if editing, otherwise it's not shown in an input field
        mutableStateOf(if (isEditing) (record.amount / 100.0).toString() else "")
    }
    var descriptionText by remember(record.description, isEditing) {
        mutableStateOf(if (isEditing) record.description ?: "" else "")
    }

    val context = LocalContext.current

    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = cardColors
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Details",
                    style = MaterialTheme.typography.titleLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = record.isComplete,
                        onCheckedChange = { onCompletionToggle() }
                    )
                    Text(
                        "Completed",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            ContactDisplayRow(contact)

            DetailRow(
                label = "Type",
                value = (type?.name ?: TypeConstants.getTypeName(record.typeId)), // Use typeId as fallback
                isEditing = false
            )

            if (isEditing) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it // Update local state
                        onAmountChange(it) // Pass raw string to ViewModel for parsing
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text(CurrencyUtils.getCurrencySymbol(context)) }
                )
            } else {
                DetailRow(
                    label = "Amount",
                    // Assuming amount is stored in cents
                    value = "${CurrencyUtils.getCurrencySymbol(context)} ${record.amount}",
                    isEditing = false
                )
            }

            DetailRow(
                label = "Date",
                value = dateFormatter.format(Date(record.date)),
                isEditing = false // Date is not editable in this component
            )

            if (isEditing) {
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = {
                        descriptionText = it // Update local state
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

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Created: ${SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(record.createdAt))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Updated: ${SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(record.updatedAt))}",
                    style = MaterialTheme.typography.labelSmall,
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
    isEditing: Boolean // Parameter kept for potential future use or consistency
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ContactDisplayRow(
    contact: Contact?
) {
    val context = LocalContext.current
    val validContactId = contact?.contactId?.toLongOrNull()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) { // Allow name to take available space
            DetailRow(
                label = "Contact",
                value = contact?.name ?: "Unknown Contact",
                isEditing = false,
            )
        }

        if (validContactId != null) { // Only show button if contact is valid
            Button(
                onClick = {
                    openContactDetails(context = context, contactId = validContactId)
                },
                modifier = Modifier.padding(start = 8.dp) // Add some spacing
            ) {
                Text(text = "View Details")
            }
        }
    }
}

@Preview(showBackground = true, name = "TransactionDetailsCard - View Mode")
@Composable
fun TransactionDetailsCardPreview() {
    AichoPaichoTheme {
        val sampleRecord = Record(
            id = "1", userId = "user1", contactId = "contact1", typeId = TypeConstants.LENT_ID,
            amount = 12550, // e.g. 125.50
            date = System.currentTimeMillis() - 86400000L * 5, // 5 days ago
            description = "Lunch with client. Discussed project milestones and future collaboration opportunities.",
            isComplete = false,
            createdAt = System.currentTimeMillis() - 86400000L * 10, // 10 days ago
            updatedAt = System.currentTimeMillis() - 86400000L * 2,  // 2 days ago
            isDeleted = false
        )
        val sampleContact = Contact(id = "contact1", name = "Alex Johnson", phone = listOf("555-0101"), contactId = "101", userId = "user1")
        val sampleType = Type(id = TypeConstants.LENT_ID, name = "Lent")

        TransactionDetailsCard(
            record = sampleRecord, contact = sampleContact, type = sampleType, isEditing = false,
            onAmountChange = {}, onDescriptionChange = {}, onDateChange = {}, onCompletionToggle = {}
        )
    }
}

@Preview(showBackground = true, name = "TransactionDetailsCard - Edit Mode")
@Composable
fun TransactionDetailsCardEditingPreview() {
    AichoPaichoTheme {
        val sampleRecord = Record(
            id = "2", userId = "user1", contactId = "contact2", typeId = TypeConstants.BORROWED_ID,
            amount = 7500, // e.g. 75.00
            date = System.currentTimeMillis() - 86400000L * 3, // 3 days ago
            description = "Shared expenses for team outing.",
            isComplete = true,
            createdAt = System.currentTimeMillis() - 86400000L * 7, // 7 days ago
            updatedAt = System.currentTimeMillis() - 86400000L * 1,  // 1 day ago
            isDeleted = false
        )
        val sampleContact = Contact(id = "contact2", name = "Maria Garcia", phone = listOf("555-0202"), contactId = "102", userId = "user1")
        val sampleType = Type(id = TypeConstants.BORROWED_ID, name = "Borrowed")

        TransactionDetailsCard(
            record = sampleRecord, contact = sampleContact, type = sampleType, isEditing = true,
            onAmountChange = {}, onDescriptionChange = {}, onDateChange = {}, onCompletionToggle = {}
        )
    }
}
