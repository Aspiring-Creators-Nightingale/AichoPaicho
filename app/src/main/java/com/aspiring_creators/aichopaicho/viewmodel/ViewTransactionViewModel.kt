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
    private val startOfMonth = calendar.apply {
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
    }.timeInMillis

    init {
        _uiState.value = _uiState.value.copy(
            dateRange = startOfMonth to endOfMonth
        )
    }

    fun loadInitialData() {
        viewModelScope.launch {
            setLoading(true)
            try {
                // Load all data concurrently
                launch { loadRecordSummary() }
                launch { loadRecords() }
                launch { loadContacts() }
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
                    filteredRecords = applyFilters(records)
                )
            }
    }

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
        applyFiltersToCurrentRecords()
    }

    fun updateMoneyToQuery(query: String) {
        _uiState.value = _uiState.value.copy(moneyToQuery = query)
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

    private fun applyFilters(records: List<com.aspiring_creators.aichopaicho.data.entity.Record>): List<com.aspiring_creators.aichopaicho.data.entity.Record> {
        val currentState = _uiState.value
        return records.filter { record ->
            // Filter by completion status
            if (!currentState.showCompleted && record.isComplete) return@filter false

            // Filter by type
            currentState.selectedType?.let { typeId ->
                if (record.typeId != typeId) return@filter false
            }

            // Filter by contact name (From query)
            if (currentState.fromQuery.isNotBlank()) {
                val contactName = currentState.contacts[record.contactId]?.name ?: ""
                if (!contactName.contains(currentState.fromQuery, ignoreCase = true)) {
                    return@filter false
                }
            }

            // Filter by money to (contact name or amount)
            if (currentState.moneyToQuery.isNotBlank()) {
                val contactName = currentState.contacts[record.contactId]?.name ?: ""
                val amountString = record.amount.toString()
                if (!contactName.contains(currentState.moneyToQuery, ignoreCase = true) &&
                    !amountString.contains(currentState.moneyToQuery)) {
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