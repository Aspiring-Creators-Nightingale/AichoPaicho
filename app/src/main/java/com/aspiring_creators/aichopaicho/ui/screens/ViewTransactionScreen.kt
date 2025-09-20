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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.entity.Contact
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
    onNavigateToIndividualRecord: (String) -> Unit,
    onNavigateToContactList:(String)->Unit,
    onNavigateToContact:()->Unit
) {
    val uiState by viewTransactionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewTransactionViewModel.loadInitialData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.appThemeColor))
    ) {
        // Top Bar
        TransactionTopBar(
            onNavigateBack = onNavigateBack,
            dateRange = uiState.dateRange,
            onDateRangeSelected = { start, end ->
                viewTransactionViewModel.updateDateRange(start, end)
            },
            onContactsNavigation ={
                onNavigateToContact()
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
/*                item {
                    uiState.recordSummary?.let { summary ->
                        NetBalanceCard(
                            summary = summary,
                            onNavigateToContactList = { },
                        )
                    }
                }*/


                // Filter Section
                item {
                    TransactionFilterSection(
                        selectedType = uiState.selectedType,
                        onTypeSelected = viewTransactionViewModel::updateSelectedType,
                        fromQuery = uiState.fromQuery,
                        onFromQueryChanged = viewTransactionViewModel::updateFromQuery,
                        moneyToQuery = uiState.moneyToQuery,
                        onMoneyToQueryChanged = viewTransactionViewModel::updateMoneyToQuery,
                        onMoneyFilterApplyClicked = viewTransactionViewModel::updateMoneyFilterApplyClicked ,
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
                        onDeleteRecord = {},
                        onNavigateToContactList = {onNavigateToContactList(it)}
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