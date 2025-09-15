package com.aspiring_creators.aichopaicho.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,  // Firebase UID or UUID
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val photoUrl: String?,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

