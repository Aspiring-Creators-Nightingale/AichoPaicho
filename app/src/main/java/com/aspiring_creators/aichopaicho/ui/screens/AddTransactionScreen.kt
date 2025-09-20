package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.AmountInputField
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.ContactPickerField
import com.aspiring_creators.aichopaicho.ui.component.DateInputField
import com.aspiring_creators.aichopaicho.ui.component.LabelComponent
import com.aspiring_creators.aichopaicho.ui.component.MultiLineTextInputField
import com.aspiring_creators.aichopaicho.ui.component.QuickActionButton
import com.aspiring_creators.aichopaicho.ui.component.SegmentedLentBorrowedToggle
import com.aspiring_creators.aichopaicho.ui.component.TextComponent
import com.aspiring_creators.aichopaicho.ui.component.TypeConstants
import com.aspiring_creators.aichopaicho.viewmodel.AddTransactionViewModel
import com.aspiring_creators.aichopaicho.viewmodel.data.AddTransactionUiEvents


@Composable
fun AddTransactionScreen(
    onNavigateBack:  (() -> Unit)? = null,
    addTransactionViewModel: AddTransactionViewModel = hiltViewModel()
) {

    val uiState by addTransactionViewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle snackbar messages reactively
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    LaunchedEffect(uiState.submissionSuccessful) {
        if (uiState.submissionSuccessful) {
            snackbarHostState.showSnackbar("Transaction added successfully")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.padding(2.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    onNavigateBack?.let { navigateBack ->
                        Spacer(modifier = Modifier.size(2.dp))
                        ButtonComponent(
                            logo = R.drawable.logo_back, // or use a back icon
                            onClick = navigateBack,
//                        enabled = !uiState.isLoading,
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .width(50.dp)
                        )
                    }

                    TextComponent(
                        value = "Add New Transaction",
                        textSize = 30.sp,
                        textColor = R.color.textColor
                    )
                }

                // Type
                Row(
                    modifier = Modifier.padding(2.dp).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LabelComponent(
                        text = "Type"
                    )
                    Spacer(modifier = Modifier.size(22.dp))

                    LaunchedEffect(Unit) {
                        addTransactionViewModel.onEvent(
                            AddTransactionUiEvents.TypeSelected(TypeConstants.TYPE_LENT) // true = Lent | false = Borrowed
                        )
                    }

                    SegmentedLentBorrowedToggle(
                        onToggle = {
                            addTransactionViewModel.onEvent(
                                AddTransactionUiEvents.TypeSelected(it)
                            )
                        }
                    )
                }

                // Name
                Row(
                    modifier = Modifier.padding(2.dp).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    LabelComponent(
                        text = "Name"
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    ContactPickerField(
                        label = "Contact Name",
                        selectedContact = uiState.contact, // Pass the selected contact to clear it
                        onContactSelected = { contact ->
                            addTransactionViewModel.onEvent(
                                AddTransactionUiEvents.ContactSelected(contact)
                            )
                        }
                    )
                }
                // Amount
                Row(
                    modifier = Modifier.padding(2.dp).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    LabelComponent(
                        text = "Amount"
                    )

                    Spacer(modifier = Modifier.size(12.dp))
                    AmountInputField(
                        label = "Amount",
                        value = uiState.amount?.toString() ?: "", // Pass the current value to clear it
                        onAmountTextChange = {
                            addTransactionViewModel.onEvent(
                                AddTransactionUiEvents.AmountEntered(it)
                            )
                        },
                    )
                }
                Row(
                    modifier = Modifier.padding(2.dp).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    LabelComponent(
                        text = "Date"
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    DateInputField(
                        label = "Date",
                        onDateSelected = {
                            addTransactionViewModel.onEvent(
                                AddTransactionUiEvents.DateEntered(it!!)
                            )
                        }, // Pass the selected date
                        initializeWithCurrentDate = true,
                        selectedDate = uiState.date
                    )
                }
                Row(
                    modifier = Modifier.padding(2.dp).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    LabelComponent(
                        text = "Description"
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    MultiLineTextInputField(
                        label = "Description",
                        value = uiState.description ?: "", // Pass the current value to clear it
                        onValueChange = {
                            addTransactionViewModel.onEvent(
                                AddTransactionUiEvents.DescriptionEntered(it)
                            )
                        }
                    )
                }


                Spacer(modifier = Modifier.size(55.dp))
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                QuickActionButton(
                    text = "Save",
                    onClick = {
                        if(!uiState.isLoading) {
                            addTransactionViewModel.onEvent(
                                AddTransactionUiEvents.Submit
                            )
                        }
                    },
                    contentDescription = "Save Button",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.size(22.dp))

                onNavigateBack?.let { navigateBack ->
                    Spacer(modifier = Modifier.size(2.dp))
                    QuickActionButton(
                        text = "Cancel",
                        onClick = {
                            if (!uiState.isLoading) {
                                navigateBack()
                            }
                        },
                        contentDescription = "Cancel Button",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddTransactionPreview()
{
    AddTransactionScreen(onNavigateBack = {})
}