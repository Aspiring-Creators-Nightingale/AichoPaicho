package com.aspiring_creators.aichopaicho.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "records",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Contact::class, parentColumns = ["id"], childColumns = ["contactId"]),
        ForeignKey(entity = Type::class, parentColumns = ["id"], childColumns = ["typeId"])
    ],
    indices = [Index(value = ["userId"]), Index(value = ["contactId"]), Index(value = ["typeId"])]
)
data class Record(
    @PrimaryKey val id: String,  // UUID
    val userId: String?,
    val contactId: String?,
    val typeId: Int,
    val amount: Int,
    val date: Long,
    val isComplete: Boolean = false,
    val isDeleted: Boolean = false,
    val description: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
