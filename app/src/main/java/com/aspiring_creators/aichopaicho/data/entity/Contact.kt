package com.aspiring_creators.aichopaicho.data.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["contactId"],  unique = true) , Index(value = ["userId"])]
)
data class Contact(
    @PrimaryKey val id: String,  // UUID
    val name: String,
    val userId: String?,
    val phone: List<String?> ,
    val contactId: String?, // system contact ID
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
