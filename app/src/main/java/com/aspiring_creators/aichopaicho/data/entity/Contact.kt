package com.aspiring_creators.aichopaicho.data.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    indices = [Index(value = ["contactId"], unique = true)]
)
data class Contact(
    @PrimaryKey val id: String,  // UUID
    val name: String,
    val phone: List<String?> ,
    val contactId: String?, // system contact ID
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
