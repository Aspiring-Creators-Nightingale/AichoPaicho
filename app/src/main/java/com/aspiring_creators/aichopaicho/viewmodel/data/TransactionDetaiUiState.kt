package com.aspiring_creators.aichopaicho.viewmodel.data

import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.Type

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val record: Record? = null,
    val contact: Contact? = null,
    val type: Type? = null
)