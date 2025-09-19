// ViewTransactionViewModel.kt
package com.aspiring_creators.aichopaicho.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspiring_creators.aichopaicho.data.entity.*
import com.aspiring_creators.aichopaicho.data.repository.*
import com.aspiring_creators.aichopaicho.viewmodel.data.ViewTransactionViewModelUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.get

@HiltViewModel
class ViewTransactionViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val userRecordSummaryRepository: UserRecordSummaryRepository,
    private val contactRepository: ContactRepository,
    private val typeRepository: TypeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViewTransactionViewModelUiState())
    val uiState: StateFlow<ViewTransactionViewModelUiState> = _uiState.asStateFlow()

    // Initialize with current month date range
    private val currentTime = System.currentTimeMillis()
    private val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
/*    private val startOfMonth = calendar.apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private val endOfMonth = calendar.apply {
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.timeInMillis*/

    init {
        _uiState.value = _uiState.value.copy(
            dateRange = Long.MIN_VALUE to Long.MAX_VALUE
        )
    }

    fun loadInitialData() {
        viewModelScope.launch {
            setLoading(true)
            try {
                // Load all data concurrently
                launch { loadRecordSummary() }
                launch { loadContacts() }
                launch { loadRecords() }
                launch { loadTypes() }
            } catch (e: Exception) {
                setErrorMessage(e.message ?: "Unknown error occurred")
            } finally {
                setLoading(false)
            }
        }
    }

    private suspend fun loadRecordSummary() {
        val (startDate, endDate) = _uiState.value.dateRange
        userRecordSummaryRepository.getCurrentUserSummaryByDate(startDate, endDate)
            .catch { e -> setErrorMessage("Failed to load summary: ${e.message}") }
            .collect { summary ->
                _uiState.value = _uiState.value.copy(recordSummary = summary)
            }
    }

    private suspend fun loadRecords() {
        val (startDate, endDate) = _uiState.value.dateRange
        recordRepository.getRecordsByDateRange(startDate, endDate)
            .catch { e -> setErrorMessage("Failed to load records: ${e.message}") }
            .collect { records ->
                _uiState.value = _uiState.value.copy(
                    records = records,
                    filteredRecords = applyFilters(records),
//                    lentContacts = contactPreviews.first,
//                    borrowedContacts = contactPreviews.second
                )
            }
    }

  /*  private fun calculateContactPreviews(records: List<Record>):
            Pair<List<ContactPreview>, List<ContactPreview>> {
        val currentContacts = _uiState.value.contacts


        // Group records by (contactId, typeId) and sum amounts
        val contactSummary: Map<Pair<String?, Int?>, Double> = records
            .filter { !it.isDeleted }
            .groupBy { it.contactId to it.typeId }
            .mapValues { (_, list) -> list.sumOf { it.amount.toDouble() } }

        val lentContacts = mutableListOf<ContactPreview>()
        val borrowedContacts = mutableListOf<ContactPreview>()

        // Iterate properly with destructuring
        contactSummary.forEach { (key, totalAmount) ->
            val (contactId, typeId) = key

            // skip null contact ids or non-positive totals
            if (contactId == null || totalAmount <= 0) return@forEach

            val contact = currentContacts[contactId] ?: return@forEach

            val contactPreview = ContactPreview(
                id = contactId,
                name = contact.name,
                amount = totalAmount
            )

            when (typeId) {
                1 -> lentContacts.add(contactPreview)
                2 -> borrowedContacts.add(contactPreview)
                else -> {
                    // unknown type - ignore or log if needed
                }
            }
        }

        return lentContacts.sortedByDescending { it.amount } to
                borrowedContacts.sortedByDescending { it.amount }
    }
*/

    private suspend fun loadContacts() {
        contactRepository.getAllContacts()
            .catch { e -> setErrorMessage("Failed to load contacts: ${e.message}") }
            .collect { contacts ->
                val contactMap = contacts.associateBy { it.id }
                _uiState.value = _uiState.value.copy(contacts = contactMap)
            }
    }

    private suspend fun loadTypes() {
        typeRepository.getAllTypes()
            .catch { e -> setErrorMessage("Failed to load types: ${e.message}") }
            .collect { types ->
                val typeMap = types.associateBy { it.id }
                _uiState.value = _uiState.value.copy(types = typeMap)
            }
    }

    fun updateDateRange(startDate: Long, endDate: Long) {
        _uiState.value = _uiState.value.copy(dateRange = startDate to endDate)
        viewModelScope.launch {
            loadRecordSummary()
            loadRecords()
        }
    }

    fun updateSelectedType(typeId: Int?) {
        _uiState.value = _uiState.value.copy(selectedType = typeId)
        applyFiltersToCurrentRecords()
    }

    fun updateFromQuery(query: String) {
        _uiState.value = _uiState.value.copy(fromQuery = query)
    }

    fun updateMoneyToQuery(query: String) {
        _uiState.value = _uiState.value.copy(moneyToQuery = query)
    }

    fun updateMoneyFilterApplyClicked() {
        applyFiltersToCurrentRecords()
    }

    fun updateShowCompleted(showCompleted: Boolean) {
        _uiState.value = _uiState.value.copy(showCompleted = showCompleted)
        applyFiltersToCurrentRecords()
    }

    private fun applyFiltersToCurrentRecords() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            filteredRecords = applyFilters(currentState.records)
        )
    }

    private fun applyFilters(records: List<Record>): List<Record> {
        val currentState = _uiState.value
        return records.filter { record ->
            // Filter by completion status
            if (!currentState.showCompleted && record.isComplete) return@filter false

            // Filter by type
            currentState.selectedType?.let { typeId ->
                if (record.typeId != typeId) return@filter false
            }

            // Filter by contact name (From query)
            if (currentState.fromQuery.isNotBlank() && currentState.moneyToQuery.isNotBlank()) {
                val amountString = record.amount
                if ( !(amountString >= uiState.value.fromQuery.toInt()  && amountString <= uiState.value.moneyToQuery.toInt())) {
                    return@filter false
                }
            }
            true
        }
    }

    fun toggleRecordCompletion(recordId: String) {
        viewModelScope.launch {
            try {
                val currentRecord = _uiState.value.records.find { it.id == recordId }
                currentRecord?.let { record ->
                    val updatedRecord = record.copy(
                        isComplete = !record.isComplete,
                        updatedAt = System.currentTimeMillis()
                    )
                    recordRepository.updateRecord(updatedRecord)
                }
            } catch (e: Exception) {
                setErrorMessage("Failed to update record: ${e.message}")
            }
        }
    }

    fun deleteRecord(recordId: String) {
        viewModelScope.launch {
            try {
                recordRepository.deleteRecord(recordId)
            } catch (e: Exception) {
                setErrorMessage("Failed to delete record: ${e.message}")
            }
        }
    }

    fun setErrorMessage(value: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = value)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun setLoading(value: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = value)
    }
}

// ContactPreview data class
data class ContactPreview(
    val id: String,
    val name: String,
    val amount: Double
)

// Updated UI State
