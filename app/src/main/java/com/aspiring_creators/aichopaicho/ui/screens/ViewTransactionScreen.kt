package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aspiring_creators.aichopaicho.ui.component.NetBalanceCard
import com.aspiring_creators.aichopaicho.ui.component.TransactionCard
import com.aspiring_creators.aichopaicho.ui.component.TransactionFilterSection
import com.aspiring_creators.aichopaicho.ui.component.TransactionTopBar
import com.aspiring_creators.aichopaicho.viewmodel.ViewTransactionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTransactionScreen(
    viewTransactionViewModel: ViewTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToIndividualRecord: (String) -> Unit
) {
    val uiState by viewTransactionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewTransactionViewModel.loadInitialData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        TransactionTopBar(
            onNavigateBack = onNavigateBack,
            dateRange = uiState.dateRange,
            onDateRangeSelected = { start, end ->
                viewTransactionViewModel.updateDateRange(start, end)
            }
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Net Balance Section
                item {
                    uiState.recordSummary?.let { summary ->
                        NetBalanceCard(
                            summary = summary,
                            onNavigateToContactList = {  },
                            lentContacts = emptyList(),
                            borrowedContacts = emptyList(),
                            onContactClick = {}
                        )
                    }
                }

                // Filter Section
                item {
                    TransactionFilterSection(
                        selectedType = uiState.selectedType,
                        onTypeSelected = viewTransactionViewModel::updateSelectedType,
                        fromQuery = uiState.fromQuery,
                        onFromQueryChanged = viewTransactionViewModel::updateFromQuery,
                        moneyToQuery = uiState.moneyToQuery,
                        onMoneyToQueryChanged = viewTransactionViewModel::updateMoneyToQuery,
                        showCompleted = uiState.showCompleted,
                        onShowCompletedChanged = viewTransactionViewModel::updateShowCompleted
                    )
                }

                // Transaction List
                items(uiState.filteredRecords) { record ->
                    TransactionCard(
                        record = record,
                        contact = uiState.contacts[record.contactId],
                        onRecordClick = { onNavigateToIndividualRecord(record.id) },
                        onCompletionToggle = {
                            viewTransactionViewModel.toggleRecordCompletion(record.id)
                        },
                        onDeleteRecord = {
                            viewTransactionViewModel.deleteRecord(record.id)
                        },
                        onNavigateToContactList = {}
                    )
                }
            }
        }

        uiState.errorMessage?.let { error ->
            LaunchedEffect(error) {
                // Handle error display (you might want to use SnackbarHost here)
            }
        }
    }
}

