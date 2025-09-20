package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aspiring_creators.aichopaicho.ui.component.TransactionDetailsCard
import com.aspiring_creators.aichopaicho.viewmodel.TransactionDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transactionId: String,
    transactionDetailViewModel: TransactionDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by transactionDetailViewModel.uiState.collectAsStateWithLifecycle()
    var isEditing by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) } // Added state for dialog


    LaunchedEffect(transactionId) {
    if(transactionId == "")
    {
        onNavigateBack()
    }
        transactionDetailViewModel.loadRecord(transactionId)
    }

    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        transactionDetailViewModel.deleteRecord()
                        showDeleteConfirmationDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmationDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Transaction Details") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                if (uiState.record != null) {
                    IconButton(
                        onClick = {
                            if (isEditing) {
                                transactionDetailViewModel.saveRecord()
                                isEditing = false
                            } else {
                                isEditing = true
                            }
                        }
                    ) {
                        Icon(
                            if (isEditing) Icons.Default.Done else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Save" else "Edit"
                        )
                    }

                    IconButton(
                        onClick = {
                            showDeleteConfirmationDialog = true
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
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
            uiState.record?.let { record ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Record Details Card
                    TransactionDetailsCard (
                        record = record,
                        contact = uiState.contact,
                        type = uiState.type,
                        isEditing = isEditing,
                        onAmountChange = transactionDetailViewModel::updateAmount,
                        onDescriptionChange = transactionDetailViewModel::updateDescription,
                        onDateChange = transactionDetailViewModel::updateDate,
                        onCompletionToggle = transactionDetailViewModel::toggleCompletion
                    )
                }
            }
        }

        uiState.errorMessage?.let { error ->
            LaunchedEffect(error) {
                // Show error snackbar
            }
        }
    }
}

