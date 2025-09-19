package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aspiring_creators.aichopaicho.ui.component.ContactDisplayRow
import com.aspiring_creators.aichopaicho.ui.component.ContactHeadingDisplay
import com.aspiring_creators.aichopaicho.ui.component.ContactRecordCard
import com.aspiring_creators.aichopaicho.ui.component.ContactRecordTabs
import com.aspiring_creators.aichopaicho.ui.component.ContactSummaryCard
import com.aspiring_creators.aichopaicho.ui.component.EmptyRecordsCard
import com.aspiring_creators.aichopaicho.viewmodel.ContactTransactionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTransactionScreen(
    contactId: String,
    contactTransactionViewModel: ContactTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToRecord: (String) -> Unit
) {
    val uiState by contactTransactionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(contactId) {
        contactTransactionViewModel.loadContactRecords(contactId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar with Contact Info
        TopAppBar(
            title = {
                ContactHeadingDisplay(uiState.contact)
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Contact Summary Card
                item {
                    ContactSummaryCard(
                        contact = uiState.contact,
                        totalLent = uiState.totalLent,
                        totalBorrowed = uiState.totalBorrowed,
                        netBalance = uiState.netBalance,
                        showCompleted = uiState.showCompleted,
                        onShowCompletedChanged = contactTransactionViewModel::updateShowCompleted
                    )
                }

                // Tab Section
                item {
                    ContactRecordTabs(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = contactTransactionViewModel::updateSelectedTab,
                        allCount = uiState.allRecords.size,
                        lentCount = uiState.lentRecords.size,
                        borrowedCount = uiState.borrowedRecords.size
                    )
                }

                // Records List based on selected tab
                val recordsToShow = when (uiState.selectedTab) {
                    1 -> uiState.lentRecords
                    2 -> uiState.borrowedRecords
                    else -> uiState.allRecords
                }

                items(recordsToShow) { record ->
                    ContactRecordCard(
                        record = record,
                        type = uiState.types[record.typeId],
                        onRecordClick = { onNavigateToRecord(record.id) },
                        onCompletionToggle = { contactTransactionViewModel.toggleRecordCompletion(record.id) },
                        onDeleteRecord = { contactTransactionViewModel.deleteRecord(record.id) }
                    )
                }

                if (recordsToShow.isEmpty()) {
                    item {
                        EmptyRecordsCard()
                    }
                }
            }
        }

        uiState.errorMessage?.let { error ->
            LaunchedEffect(error) {
                // Handle error display
            }
        }
    }
}
