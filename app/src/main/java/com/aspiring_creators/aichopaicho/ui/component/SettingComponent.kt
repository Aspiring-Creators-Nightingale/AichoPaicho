package com.aspiring_creators.aichopaicho.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspiring_creators.aichopaicho.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.contains
import kotlin.text.isNotEmpty
import kotlin.text.lowercase


@Composable
fun UserProfileCard(
    user: com.aspiring_creators.aichopaicho.data.entity.User?,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // space around the card
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // optional, helps if card height is big
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                if (user?.name?.isNotEmpty() == true) {
                    Text(
                        text = user.name.first().uppercase(),
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

            Spacer(modifier = Modifier.height(12.dp))

            if (user?.isOffline == true) {
                Text(
                    text = "Local Account",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Sign in to backup your data",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onSignInClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally), // force center
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.buttonColor)
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.logo_google),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign in with Google", color = Color.Black)
                }
            } else {
                Text(
                    text = user?.name ?: "Unknown User",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = user?.email ?: "",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onSignOutClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally), // center button
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}


@Composable
fun SettingsCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            content()
        }
    }
}

@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    allCurrencies: List<String>, // Rename for clarity
    expanded: Boolean,
    onToggleDropdown: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Filter currencies based on search query
    val filteredCurrencies = if (searchQuery.isEmpty()) {
        allCurrencies
    } else {
        allCurrencies.filter {
            it.lowercase(Locale.getDefault()).contains(searchQuery.lowercase(Locale.getDefault()))
        }
    }

    Box {
        OutlinedButton(
            onClick = {
                onToggleDropdown()
                if (!expanded) { // Reset search query when opening
                    searchQuery = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Currency: $selectedCurrency")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse currency selection" else "Expand currency selection"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onToggleDropdown,
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer) // Set a background for the dropdown
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search currency") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add some space


            if (filteredCurrencies.isEmpty()) {
                DropdownMenuItem(
                    enabled = false,
                    onClick = { },
                    text = { Text("No matching currencies") }
                )
            } else {
                filteredCurrencies.forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            onToggleDropdown()
                            onCurrencySelected(currency)
                                  },
                        text = { Text(currency) }
                    )
                }
            }

        }
    }
}


@Composable
fun BackupSyncSettings(
    isBackupEnabled: Boolean,
    onToggleBackup: () -> Unit,
    isSyncing: Boolean,
    syncProgress: Float,
    syncMessage: String,
    lastSyncTime: Long?,
    onStartSync: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Backup")
            Switch(
                checked = isBackupEnabled,
                onCheckedChange = { onToggleBackup() }
            )
        }

        if (isBackupEnabled) {
            if (isSyncing) {
                Column {
                    LinearProgressIndicator(
                    progress = { syncProgress },
                    modifier = Modifier.fillMaxWidth(),
                    color = ProgressIndicatorDefaults.linearColor,
                    trackColor = ProgressIndicatorDefaults.linearTrackColor,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                    Text(
                        text = syncMessage,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Manual Backup", fontSize = 14.sp)
                        lastSyncTime?.let {
                            Text(
                                text = "Last: ${dateFormatter.format(Date(it))}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = onStartSync,
                        enabled = !isSyncing
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Backup Now")
                    }
                }
            }
        }
    }
}

@Composable
fun AppInformation(
    version: String,
    buildNumber: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Version")
            Text(version, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Build")
            Text(buildNumber, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AboutSection() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Privacy Policy",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Open privacy policy */ }
                .padding(vertical = 8.dp)
        )
        Text(
            text = "Terms of Service",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Open terms */ }
                .padding(vertical = 8.dp)
        )
        Text(
            text = "Contact Support",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Open support */ }
                .padding(vertical = 8.dp)
        )
    }
}