package com.aspiring_creators.aichopaicho.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow // Keep FlowRow if already used
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import coil3.toUri
// import coil3.request.error // Already imported via ImageRequest.Builder
// import coil3.request.placeholder // Already imported via ImageRequest.Builder
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.entity.User
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme // Added for previews
import com.aspiring_creators.aichopaicho.viewmodel.data.DashboardScreenUiState

@Composable
fun UserProfileImage(
    photoUrl: String?, // Made nullable to handle potential null from user?.photoUrl
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
                .data(photoUrl ?: R.drawable.placeholder_user_profile) // Fallback if URL is null
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
                    is AsyncImagePainter.State.Loading -> {
                        Log.d("UserProfileImage", "Loading image: $photoUrl")
                    }
                    is AsyncImagePainter.State.Error -> {
                        Log.e(
                            "UserProfileImage",
                            "Error loading image: $photoUrl",
                            state.result.throwable
                        )
                    }
                    is AsyncImagePainter.State.Success -> {
                        Log.d("UserProfileImage", "Successfully loaded image: $photoUrl")
                    }
                    else -> {
                        Log.i("UserProfileImage", "Image state: $state for url: $photoUrl")
                    }
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
            photoUrl = "https://example.com/image.jpg", // Use a placeholder or ensure network for actual URL
            userName = "Goodness",
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun ErrorContent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Added padding
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content
        ) {
            TextComponent(
                value = "Error: $errorMessage",
                textSize = 18.sp, // Adjusted size
                color = MaterialTheme.colorScheme.error // Use theme error color
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonComponent(
                vectorLogo = Icons.Default.Refresh,
                text = "Retry",
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f) // Control width
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
fun DashboardContent(
    uiState: DashboardScreenUiState,
    onNavigateToAddTransaction: (() -> Unit)?,
    onNavigateToViewTransactions: (() -> Unit)?,
    onNavigateToSettings: (() -> Unit)?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // .padding(16.dp) // Padding applied by LazyColumn in DashboardScreen
    ) {
        UserDashboardToast(uiState) // This is the user card

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge, // Using titleLarge for section header
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp) // Adjusted padding
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), // Padding for FlowRow content
            horizontalArrangement = Arrangement.spacedBy(12.dp), // Increased spacing
            verticalArrangement = Arrangement.spacedBy(12.dp), // Increased spacing
        ) {
            onNavigateToAddTransaction?.let { navigate ->
                QuickActionButton(
                    text = "New Txn", // Shorter text
                    onClick = navigate,
                    contentDescription = "Add new Record or Transaction"
                )
            }
            onNavigateToViewTransactions?.let { navigate ->
                QuickActionButton(
                    text = "View Txns", // Shorter text
                    onClick = navigate,
                    contentDescription = "View Transactions"
                )
            }
            onNavigateToSettings?.let { navigate ->
                QuickActionButton(
                    text = "Settings",
                    onClick = navigate,
                    contentDescription = "Settings"
                )
            }
        }

        // Error message display (if any, specific to DashboardContent's operations, not general screen errors)
        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun UserDashboardToast(uiState: DashboardScreenUiState) { // Renamed Preview to avoid conflict
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Slightly reduced elevation
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Use surfaceVariant
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant // Use onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserProfileImage(
                photoUrl = uiState.user?.photoUrl.toString(),
                userName = uiState.user?.name,
                modifier = Modifier.size(56.dp) // Adjusted size
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Welcome back!",
                    style = MaterialTheme.typography.titleMedium
                    // Color will be inherited from Card's contentColor (onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = uiState.user?.name ?: "User",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Explicit primary color for name
                )

                uiState.user?.email?.let { email ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall, // Slightly smaller for email
                        // color = MaterialTheme.colorScheme.onSurfaceVariant (inherited)
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDashboardToastPreview() { // Renamed from DashboardScreenPreview to be specific
    AichoPaichoTheme {
        UserDashboardToast(
            uiState = DashboardScreenUiState(
                user = User( // Mock user data
                    name = "Alex Doe",
                    email = "alex.doe@example.com",
                    photoUrl = " ".toUri() ,
                    id = "df"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardContentPreview() { // Specific preview for DashboardContent
    AichoPaichoTheme {
        DashboardContent(
            uiState = DashboardScreenUiState(
                user =User(
                    name = "Jane Doe",
                    email = "jane.doe@example.com",
                    photoUrl = "".toUri(),
                    id = "df"
                )
            ),
            onNavigateToAddTransaction = {},
            onNavigateToViewTransactions = {},
            onNavigateToSettings = {}
        )
    }
}

