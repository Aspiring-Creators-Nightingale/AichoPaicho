package com.aspiring_creators.aichopaicho.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.UserRecordSummary
import com.aspiring_creators.aichopaicho.viewmodel.ContactPreview
import java.text.SimpleDateFormat
import java.util.*
import kotlin.String
import kotlin.Unit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTopBar(
    onNavigateBack: () -> Unit,
    dateRange: Pair<Long, Long>,
    onDateRangeSelected: (Long, Long) -> Unit
) {

    val dateFormatter = remember { SimpleDateFormat("dd MMM yy", Locale.getDefault()) }
    val dateRangeText =
        "${dateFormatter.format(Date(dateRange.first))} – ${dateFormatter.format(Date(dateRange.second))}"

    var showDatePickerDialog by remember { mutableStateOf(false) }

    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = if (dateRange.first != Long.MIN_VALUE) dateRange.first else null,
        initialSelectedEndDateMillis = if (dateRange.second != Long.MAX_VALUE) dateRange.second else null,
    )


    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {  showDatePickerDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date range",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateRangeText,
                        fontSize = 14.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                        val startDate = dateRangePickerState.selectedStartDateMillis
                        val endDate = dateRangePickerState.selectedEndDateMillis
                        if (startDate != null && endDate != null) {
                            onDateRangeSelected(startDate, endDate)
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePickerDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            // The DateRangePicker composable itself
            DateRangePicker(state = dateRangePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
TransactionTopBar(
    onNavigateBack = {},
    dateRange = System.currentTimeMillis() - 122323 to System.currentTimeMillis(),
    onDateRangeSelected = { _, _ -> }
)
}

@Composable
fun NetBalanceCard(
    summary: UserRecordSummary,
    onNavigateToContactList: (String) -> Unit,
    lentContacts: List<ContactPreview> = emptyList(),
    borrowedContacts: List<ContactPreview> = emptyList(),
    onContactClick: (String) -> Unit = { id -> onNavigateToContactList(id) }
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Header area: clickable toggles expand state
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Net Balance",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "NPR ${summary.netTotal.toInt()}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (summary.netTotal >= 0)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BalanceMiniItem("Lent", summary.totalLent, true)
                    Spacer(modifier = Modifier.width(12.dp))
                    BalanceMiniItem("Borrowed", summary.totalBorrowed, false)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Extended area with smooth animation
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 4.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BalanceItemExtended(
                            label = "Lent",
                            amount = summary.totalLent,
                            isPositive = true,
                            icon = Icons.Default.KeyboardArrowUp,
                            count = summary.lentContactsCount,
                            contacts = lentContacts,
                            onNavigateToContactList = { onNavigateToContactList(TypeConstants.TYPE_LENT) },
                            onContactClick = onContactClick,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        BalanceItemExtended(
                            label = "Borrowed",
                            amount = summary.totalBorrowed,
                            isPositive = false,
                            icon = Icons.Default.KeyboardArrowDown,
                            count = summary.borrowedContactsCount,
                            contacts = borrowedContacts,
                            onNavigateToContactList = { onNavigateToContactList(TypeConstants.TYPE_BORROWED) },
                            onContactClick = onContactClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceMiniItem(label: String, amount: Double, isPositive: Boolean) {
    val tint = if (isPositive)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isPositive) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "NPR ${amount.toInt()}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = tint
            )
        }
    }
}

@Composable
fun BalanceItemExtended(
    label: String,
    amount: Double,
    isPositive: Boolean,
    icon: ImageVector,
    count: Int,
    contacts: List<ContactPreview> = emptyList(),
    onNavigateToContactList: () -> Unit,
    onContactClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = if (isPositive)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Header with icon and label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Amount
        Text(
            text = "NPR ${amount.toInt()}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = tint
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation button
        FilledTonalButton(
            onClick = onNavigateToContactList,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = tint.copy(alpha = 0.1f),
                contentColor = tint
            )
        ) {
            Text(
                text = if (isPositive)
                    "Lent to $count ${if (count == 1) "person" else "people"}"
                else
                    "Borrowed from $count ${if (count == 1) "person" else "people"}",
                style = MaterialTheme.typography.labelMedium
            )
        }

        // Contact chips - horizontal scrolling list
        if (contacts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(contacts.size) { idx ->
                    val c = contacts[idx]
                    ContactChip(contact = c, onClick = { onContactClick(c.id) })
                }
            }
        }
    }
}

@Composable
fun ContactChip(
    contact: ContactPreview,
    onClick: () -> Unit,
    tintColor: Color = MaterialTheme.colorScheme.primary
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular avatar with initial
            Surface(
                shape = CircleShape,
                color = tintColor.copy(alpha = 0.1f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = contact.name.take(1).uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = tintColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "NPR ${contact.amount.toInt()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = tintColor
                )
            }
        }
    }
}

/* ----- Data Classes and Preview ----- */


@Preview(showBackground = true, widthDp = 360)
@Composable
fun NetBalanceCardPreview() {
    val summary = UserRecordSummary("0", 2323.0, 2222.0, 101.0, 3, 2)
    val lent = listOf(
        ContactPreview("c1", "Sita Sharma", 800.0),
        ContactPreview("c2", "Ram Bahadur", 700.0),
        ContactPreview("c3", "Gita Devi", 723.0)
    )
    val borrowed = listOf(
        ContactPreview("b1", "Hari Krishna", 1200.0),
        ContactPreview("b2", "Maya Thapa", 1022.0)
    )

    MaterialTheme {
        Surface {
            NetBalanceCard(
                summary = summary,
                onNavigateToContactList = { type -> /* navigate to screen with filter type */ },
                lentContacts = lent,
                borrowedContacts = borrowed,
                onContactClick = { id -> /* navigate to contact detail */ }
            )
        }
    }
}
@Composable
fun TransactionFilterSection(
    selectedType: Int?,
    onTypeSelected: (Int?) -> Unit,
    fromQuery: String,
    onFromQueryChanged: (String) -> Unit,
    moneyToQuery: String,
    onMoneyToQueryChanged: (String) -> Unit,
    onMoneyFilterApplyClicked: () -> Unit,
    showCompleted: Boolean,
    onShowCompletedChanged: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Compact header: label + filter button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.List,
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Filter",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // brief summary of active filters in single line (compact)
                    val summary = buildString {
                        if (selectedType == TypeConstants.LENT_ID) append("• Lent ")
                        else if (selectedType == TypeConstants.BORROWED_ID) append("• Borrowed ")
                        if (fromQuery.isNotBlank()) append("• From=$fromQuery ")
                        if (moneyToQuery.isNotBlank()) append("• ≤ $moneyToQuery")
                        if (showCompleted) append(" • Completed")
                    }
                    if (summary.isNotEmpty()) {
                        Text(
                            text = summary.trim(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(if (expanded) 90f else 0f)
                )
            }

            // Expanded area (compact controls)
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Type chips + small actions (horizontal & compact)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = selectedType == TypeConstants.LENT_ID,
                            onClick = { onTypeSelected(if (selectedType == TypeConstants.LENT_ID) null else TypeConstants.LENT_ID) },
                            label = { Text(TypeConstants.TYPE_LENT) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = selectedType == TypeConstants.BORROWED_ID,
                            onClick = { onTypeSelected(if (selectedType == TypeConstants.BORROWED_ID) null else TypeConstants.BORROWED_ID) },
                            label = { Text(TypeConstants.TYPE_BORROWED) },
                            modifier = Modifier.weight(1f)
                        )

                        // small clear button
                        TextButton(onClick = {
                            onTypeSelected(null)
                            onFromQueryChanged("")
                            onMoneyToQueryChanged("")
                            onShowCompletedChanged(false)
                        }) {
                            Text("Clear", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Amount + From fields arranged compactly
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = fromQuery,
                            onValueChange = onFromQueryChanged,
                            label = { Text("From") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = moneyToQuery,
                            onValueChange = onMoneyToQueryChanged,
                            label = { Text("Max Amount") },
                            singleLine = true,
                            modifier = Modifier.width(120.dp)
                        )
                        // Apply button
                        FilledTonalButton(
                            onClick = { onMoneyFilterApplyClicked() },
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text("Apply")
                        }
                    }

                    // Show completed toggle in a compact row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = showCompleted,
                                onCheckedChange = onShowCompletedChanged,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Show completed")
                        }

                        // collapse button
                        TextButton(onClick = { expanded = false }) {
                            Text("Done")
                        }
                    }
                }
            }
        }
    }
}

// ---------- Transaction Card (aligned & compact) ----------
@Composable
fun TransactionCard(
    record: Record,
    contact: Contact?,
    onRecordClick: () -> Unit,
    onCompletionToggle: () -> Unit,
    onDeleteRecord: () -> Unit,
    onNavigateToContactList: (String) -> Unit,
) {
    val dateFormatter = remember { SimpleDateFormat("dd/M/yy", Locale.getDefault()) }
    val isLent = record.typeId == TypeConstants.LENT_ID
    val accent = if (isLent) Color(0xFF2E7D32) else Color(0xFFC62828)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onRecordClick() }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Arrow icon (direction)
            Icon(
                painter = painterResource(
                    if (isLent) com.aspiring_creators.aichopaicho.R.drawable.arrow_up
                    else com.aspiring_creators.aichopaicho.R.drawable.arrow_down
                ),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Circle avatar (clickable) - uses contact safely
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        contact?.id?.let { onNavigateToContactList(it) }
                    },
                shape = CircleShape,
                color = accent,
                tonalElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = contact?.name?.firstOrNull()?.uppercase() ?: "?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact?.name ?: "Unknown",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (record.isComplete) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (record.isComplete) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dateFormatter.format(Date(record.date)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isLent) "+" else "-"} NPR.${record.amount}",
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    textDecoration = if (record.isComplete) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Completion checkbox: small
                    Checkbox(
                        checked = record.isComplete,
                        onCheckedChange = { onCompletionToggle() }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Delete icon small
/*                    IconButton(onClick = onDeleteRecord) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }*/
                }
            }
        }
    }
}

// ---------- Previews (simple) ----------
@Preview(showBackground = true, widthDp = 360)
@Composable
fun TransactionFilterSectionPreview() {
    var type by remember { mutableStateOf<Int?>(2) }
    var from by remember { mutableStateOf("") }
    var money by remember { mutableStateOf("") }
    var showCompleted by remember { mutableStateOf(false) }

    TransactionFilterSection(
        selectedType = type,
        onTypeSelected = { type = it },
        fromQuery = from,
        onFromQueryChanged = { from = it },
        moneyToQuery = money,
        onMoneyToQueryChanged = { money = it },
        onMoneyFilterApplyClicked = {},
        showCompleted = showCompleted,
        onShowCompletedChanged = { showCompleted = it }
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun TransactionCardPreview() {
    TransactionCard(
        record = Record("","","89474dfsdf-dfsdf",1,2323,System.currentTimeMillis(),false,false,""),
        contact = Contact("c1","Golesam",listOf("987"),"",false),
        onRecordClick = {},
        onCompletionToggle = {},
        onDeleteRecord = {},
        onNavigateToContactList = {}
    )
}