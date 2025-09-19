package com.aspiring_creators.aichopaicho.viewmodel.data

import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Type

import com.aspiring_creators.aichopaicho.data.entity.Record

data class ContactTransactionUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val contact: Contact? = null,
    val allRecords: List<Record> = emptyList(),
    val lentRecords: List<Record> = emptyList(),
    val borrowedRecords: List<Record> = emptyList(),
    val types: Map<Int, Type> = emptyMap(),
    val totalLent: Double = 0.0,
    val totalBorrowed: Double = 0.0,
    val netBalance: Double = 0.0,
    val showCompleted: Boolean = true,
    val selectedTab: Int = 0 // 0 = All, 1 = Lent, 2 = Borrowed
)