package com.aspiring_creators.aichopaicho.ui.component

import android.util.Log
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card // Used by UserDashboardToast, NetBalanceCard
import androidx.compose.material3.CardDefaults // Used by Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text // Standard Text, used alongside TextComponent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.aspiring_creators.aichopaicho.CurrencyUtils
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.entity.*
import com.aspiring_creators.aichopaicho.data.entity.User
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme
import com.aspiring_creators.aichopaicho.viewmodel.ContactPreview
import com.aspiring_creators.aichopaicho.viewmodel.data.DashboardScreenUiState

@Composable
fun UserProfileImage( // No changes from previous plan, looks good
    photoUrl: String?,
    userName: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl ?: R.drawable.placeholder_user_profile)
                .crossfade(true)
                .placeholder(R.drawable.placeholder_user_profile)
                .error(R.drawable.placeholder_user_profile_error)
                .build(),
            contentDescription = "Profile photo of ${userName ?: "user"}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            onState = { state ->
                when (state) {
                    is AsyncImagePainter.State.Loading -> Log.d("UserProfileImage", "Loading image: $photoUrl")
                    is AsyncImagePainter.State.Error -> Log.e("UserProfileImage", "Error loading image: $photoUrl", state.result.throwable)
                    is AsyncImagePainter.State.Success -> Log.d("UserProfileImage", "Successfully loaded image: $photoUrl")
                    else -> Log.i("UserProfileImage", "Image state: $state for url: $photoUrl")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileImagePreview() {
    AichoPaichoTheme {
        UserProfileImage(
            photoUrl = null,
            userName = "Goodness",
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun ErrorContent( // Uses TextComponent and ButtonComponent from AppComponent.kt
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextComponent( // Using TextComponent from AppComponent.kt
                value = "Error: $errorMessage",
                // textSize = 18.sp, // TextComponent default is 12.sp, use its default or pass explicit M3 typography based size
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonComponent( // Using ButtonComponent from AppComponent.kt
                vectorLogo = Icons.Default.Refresh,
                text = "Retry",
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentPreview() {
    AichoPaichoTheme {
        ErrorContent(
            errorMessage = "A network error occurred.",
            onRetry = {}
        )
    }
}

@Composable
fun UserDashboardToast(uiState: DashboardScreenUiState) { // User Welcome Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserProfileImage(
                photoUrl = uiState.user?.photoUrl?.toString(),
                userName = uiState.user?.name,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text( // Standard Text for consistency within this card
                    text = "Welcome back!",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text( // Standard Text
                    text = uiState.user?.name ?: "User",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                uiState.user?.email?.let { email ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text( // Standard Text
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDashboardToastPreview() {
    AichoPaichoTheme {
        UserDashboardToast(
            uiState = DashboardScreenUiState(
                user = User(
                    id = "user1",
                    name = "Alex Doe",
                    email = "alex.doe@example.com",
                    photoUrl = null
                )
            )
        )
    }
}

@Composable
fun DashboardContent( // Uses QuickActionButton from AppComponent.kt
    uiState: DashboardScreenUiState,
    onNavigateToAddTransaction: (() -> Unit)?,
    onNavigateToViewTransactions: (() -> Unit)?,
    onNavigateToSettings: (() -> Unit)?,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        UserDashboardToast(uiState)
        Spacer(modifier = Modifier.height(24.dp))
        Text( // Standard Text for section header
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2 // Keeps the 2-item row structure
        ) {
            onNavigateToAddTransaction?.let { navigate ->
                QuickActionButton( // Using QuickActionButton from AppComponent.kt
                    text = "New Txn",
                    onClick = navigate,
                    contentDescription = "Add new Transaction",
                    modifier = Modifier.weight(1f)
                )
            }
            onNavigateToViewTransactions?.let { navigate ->
                QuickActionButton( // Using QuickActionButton from AppComponent.kt
                    text = "View Txns",
                    onClick = navigate,
                    contentDescription = "View Transactions",
                    modifier = Modifier.weight(1f)
                )
            }
            onNavigateToSettings?.let { navigate ->
                QuickActionButton( // Using QuickActionButton from AppComponent.kt
                    text = "Settings",
                    onClick = navigate,
                    contentDescription = "Open Settings",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text( // Standard Text for error
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
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
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp) // Keep padding from DashboardScreen in mind
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Slightly less elevation than before
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Themed container
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant // Themed content
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Net Balance",
                        style = MaterialTheme.typography.titleMedium
                        // Color from Card's contentColor
                    )
                    Text(
                        "${CurrencyUtils.getCurrencyCode(context)} ${summary.netTotal.toInt()}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (summary.netTotal >= 0) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Keep as is, good contrast
                    )
                }
            }

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
                        .padding(start = 8.dp, end = 8.dp, bottom = 10.dp, top = 2.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outlineVariant // Themed divider
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
    val tint = if (isPositive) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }
    val context = LocalContext.current

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
                text = "${CurrencyUtils.getCurrencyCode(context)} ${amount.toInt()}",
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
    val amountColor = if (isPositive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val buttonContainerColor = if (isPositive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
    val buttonContentColor = if (isPositive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = amountColor, // Match amount color
                modifier = Modifier.size(6.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Good for secondary info
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${CurrencyUtils.getCurrencyCode(context)} ${amount.toInt()}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = amountColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilledTonalButton( // Using FilledTonalButton for less emphasis than FilledButton
            onClick = onNavigateToContactList,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = buttonContainerColor,
                contentColor = buttonContentColor
            )
        ) {
            Text(
                text = if (isPositive) {
                    "Lent to $count ${if (count == 1) "person" else "people"}"
                } else {
                    "Borrowed from $count ${if (count == 1) "person" else "people"}"
                },
                style = MaterialTheme.typography.labelMedium
            )
        }

        if (contacts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(contacts.size) { idx ->
                    val c = contacts[idx]
                    ContactChip(
                        contact = c,
                        onClick = { onContactClick(c.id) },
                        // Use tertiary for contact chips to differentiate from primary/error actions
                        baseColor = MaterialTheme.colorScheme.tertiary,
                        onBaseColor = MaterialTheme.colorScheme.onTertiary,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        onContainerColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}
