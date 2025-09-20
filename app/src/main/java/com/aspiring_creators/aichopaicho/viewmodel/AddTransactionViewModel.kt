package com.aspiring_creators.aichopaicho.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.repository.RecordRepository
import com.aspiring_creators.aichopaicho.viewmodel.data.AddTransactionUiEvents
import com.aspiring_creators.aichopaicho.viewmodel.data.AddTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.repository.ContactRepository
import com.aspiring_creators.aichopaicho.data.repository.TypeRepository
import com.aspiring_creators.aichopaicho.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val typeRepository: TypeRepository,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel(){

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    private suspend fun handleSubmit(): Boolean {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, submissionSuccessful = false) }

        return try {
            if(uiState.value.amount == null){
                throw IllegalArgumentException("Amount cannot be empty")
            }

            val type = typeRepository.getByName(uiState.value.type!!)
            val user = userRepository.getUser()

            val contactExist = contactRepository.getContactByContactId(uiState.value.contact!!.contactId!!)
            val contactToSave: Contact
            if(contactExist == null) {
                contactToSave = Contact(
                    id = UUID.randomUUID().toString(),
                    name = uiState.value.contact!!.name,
                    phone = uiState.value.contact!!.phone,
                    contactId = uiState.value.contact!!.contactId,
                    userId = user.id
                )

                contactRepository.checkAndInsert(contactToSave)
            } else {
                contactToSave = contactExist
            }

            val recordToSave = Record(
                id = UUID.randomUUID().toString(),
                userId = user.id,
                typeId = type.id,
                contactId = contactToSave.id,
                amount = uiState.value.amount!!,
                date = uiState.value.date!!,
                description = uiState.value.description
            )
            recordRepository.upsert(recordToSave)

            Log.e("AddTransactionViewModel", "handleSubmit: $recordToSave")

            // Clear form fields after successful save
            _uiState.update { currentState ->
                currentState.copy(
                    submissionSuccessful = true,
                    isLoading = false,
                    // Clear the form fields
                    contact = null,
                    amount = null,
                    description = null
                    // Note: We keep type and date as they might want to add similar transactions
                )
            }

            true // Success
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    errorMessage = e.message,
                    isLoading = false,
                    submissionSuccessful = false
                )
            }
            false // Failure
        }
    }

    fun onEvent(event: AddTransactionUiEvents) {
        when(event) {
            is AddTransactionUiEvents.TypeSelected -> {
                _uiState.value = _uiState.value.copy(
                    type = event.type,
                    // Clear any previous success/error states when user starts new input
                    submissionSuccessful = false,
                    errorMessage = null
                )
            }
            is AddTransactionUiEvents.AmountEntered -> {
                _uiState.value = _uiState.value.copy(
                    amount = event.amount.toIntOrNull(),
                    // Clear any previous success/error states when user starts new input
                    submissionSuccessful = false,
                    errorMessage = null
                )
            }
            is AddTransactionUiEvents.DateEntered -> {
                _uiState.value = _uiState.value.copy(
                    date = event.date,
                    // Clear any previous success/error states when user starts new input
                    submissionSuccessful = false,
                    errorMessage = null
                )
            }
            is AddTransactionUiEvents.DescriptionEntered -> {
                _uiState.value = _uiState.value.copy(
                    description = event.description,
                    // Clear any previous success/error states when user starts new input
                    submissionSuccessful = false,
                    errorMessage = null
                )
            }
            is AddTransactionUiEvents.ContactSelected -> {
                _uiState.value = _uiState.value.copy(
                    contact = event.contact,
                    // Clear any previous success/error states when user starts new input
                    submissionSuccessful = false,
                    errorMessage = null
                )
            }
            AddTransactionUiEvents.Submit -> {
                viewModelScope.launch {
                    handleSubmit()
                    // No need to handle snackbar here, let the screen handle it reactively
                }
            }
        }
    }
}