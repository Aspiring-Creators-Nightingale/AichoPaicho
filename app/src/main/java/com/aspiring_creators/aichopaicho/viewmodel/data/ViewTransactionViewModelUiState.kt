package com.aspiring_creators.aichopaicho.viewmodel.data

import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.Type
import com.aspiring_creators.aichopaicho.data.entity.UserRecordSummary

data class ViewTransactionViewModelUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val recordSummary: UserRecordSummary? = null,
    val records: List<Record> = emptyList(),
    val filteredRecords: List<Record> = emptyList(),
    val contacts: Map<String, Contact> = emptyMap(),
    val types: Map<Int, Type> = emptyMap(),
    val dateRange: Pair<Long, Long> = 0L to 0L,
    val selectedType: Int? = null,
    val fromQuery: String = "",
    val moneyToQuery: String = "",
    val showCompleted: Boolean = true
)