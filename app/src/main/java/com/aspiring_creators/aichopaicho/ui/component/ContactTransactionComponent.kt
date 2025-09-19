package com.aspiring_creators.aichopaicho.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
 fun ContactSummaryCard(
    contact: com.aspiring_creators.aichopaicho.data.entity.Contact?,
    totalLent: Double,
    totalBorrowed: Double,
    netBalance: Double,
    showCompleted: Boolean,
    onShowCompletedChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Contact Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                if (contact?.name?.isNotEmpty() == true) {
                    Text(
                        text = contact.name.first().uppercase(),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Net Balance
            Text(
                text = "Net Balance",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(R.string.npr) + netBalance.toInt(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    netBalance > 0 -> Color(0xFF4CAF50) // You owe them
                    netBalance < 0 -> Color(0xFFF44336) // They owe you
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            Text(
                text = when {
                    netBalance > 0 -> "You owe them"
                    netBalance < 0 -> "They owe you"
                    else -> "All settled"
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lent and Borrowed Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem(
                    label = "Lent to them",
                    amount = totalLent,
                    icon = Icons.AutoMirrored.Default.ArrowForward,
                    color = Color(0xFF4CAF50)
                )

                SummaryItem(
                    label = "Borrowed from them",
                    amount = totalBorrowed,
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    color = Color(0xFFF44336)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show Completed Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = showCompleted,
                    onCheckedChange = onShowCompletedChanged
                )
                Text("Show completed transactions")
            }
        }
    }
}

@Composable
 fun SummaryItem(
    label: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "NPR ${amount.toInt()}",
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
 fun ContactRecordTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    allCount: Int,
    lentCount: Int,
    borrowedCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            TabButton(
                text = "All ($allCount)",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Lent ($lentCount)",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Borrowed ($borrowedCount)",
                isSelected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
 fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
 fun EmptyRecordsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No records found",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No transactions match the current filters",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
fun ContactRecordCard(
    record: Record,
    type: Type?,
    onRecordClick: () -> Unit,
    onCompletionToggle: () -> Unit,
    onDeleteRecord: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val isLent = type?.name?.lowercase() == "lent"
    val cardColor = if (isLent) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountPrefix = if (isLent) "+" else "-"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecordClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Type indicator circle
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(cardColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Transaction Details
            Column(modifier = Modifier.weight(1f)) {
                // Amount and Type
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$amountPrefix NPR ${record.amount}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = cardColor,
                        textDecoration = if (record.isComplete) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Text(
                        text = type?.name ?: "Unknown",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .background(
                                color = cardColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Date and Time
                Text(
                    text = "${dateFormatter.format(Date(record.date))} at ${timeFormatter.format(Date(record.date))}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Description if available
                record.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = if (record.isComplete) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            // Completion Status and Actions
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Completion Checkbox
                Checkbox(
                    checked = record.isComplete,
                    onCheckedChange = { onCompletionToggle() },
                    modifier = Modifier.size(20.dp)
                )

                // Delete Button
                IconButton(
                    onClick = onDeleteRecord,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun ContactHeadingDisplay(
    contact: Contact?,
) {
    val context = LocalContext.current
    val validContactId = contact?.contactId?.toLongOrNull() // Safely convert to Long

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween, // Or Arrangement.SpaceEvenly
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = contact?.name ?: "Unknown",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
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