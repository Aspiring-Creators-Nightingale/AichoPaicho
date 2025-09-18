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

//    val uiState =  mutableStateOf(AddTransactionUiState())
private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    private fun handleSubmit()
    {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, submissionSuccessful = false) }

            try{
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
                    )

                     contactRepository.checkAndInsert(contactToSave)
                }else{
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

                    _uiState.update { it.copy(submissionSuccessful = true, isLoading = false) }
                }catch (e: Exception){
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false, submissionSuccessful = false) }
            }
        }
    }


    fun onEvent(event: AddTransactionUiEvents)
    {
        when(event)
        {
            is AddTransactionUiEvents.TypeSelected -> {
                _uiState.value = _uiState.value.copy(
                    type = event.type
                )
            }
            is AddTransactionUiEvents.AmountEntered -> {
                _uiState.value = _uiState.value.copy(
                    amount = event.amount.toIntOrNull()
                )
            }
            is AddTransactionUiEvents.DateEntered -> {
                _uiState.value = _uiState.value.copy(
                    date = event.date
                )
            }
            is AddTransactionUiEvents.DescriptionEntered -> {
                _uiState.value = _uiState.value.copy(
                    description = event.description
                )
            }
            is AddTransactionUiEvents.ContactSelected -> {
                _uiState.value = _uiState.value.copy(
                    contact = event.contact
                )
            }
            AddTransactionUiEvents.Submit -> {
                handleSubmit()
            }

        }
    }


}