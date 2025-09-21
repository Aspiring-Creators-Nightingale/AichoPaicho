package com.aspiring_creators.aichopaicho.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card // Used by UserDashboardToast, NetBalanceCard
import androidx.compose.material3.CardDefaults // Used by Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text // Standard Text, used alongside TextComponent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.entity.*
import com.aspiring_creators.aichopaicho.data.entity.User
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme
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



// NetBalanceCard is specific to Dashboard, so its M3 themed version remains here.
// LoadingContent and NotSignedInContent are assumed to be used from AppComponent.kt, so their definitions are removed from here.
@Composable
fun NetBalanceCard(
    summary: UserRecordSummary,
    onNavigateToContactList: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Overall Balance", style = MaterialTheme.typography.titleMedium) // Standard Text
            Text( // Standard Text
                text = "%.2f".format(summary.netTotal),
                style = MaterialTheme.typography.displaySmall,
                color = if (summary.netTotal >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Lent", style = MaterialTheme.typography.labelMedium) // Standard Text
                    Text("%.2f".format(summary.totalLent), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary) // Standard Text
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Borrowed", style = MaterialTheme.typography.labelMedium) // Standard Text
                    Text("%.2f".format(summary.totalBorrowed), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error) // Standard Text
                }
            }
        }
    }
}
