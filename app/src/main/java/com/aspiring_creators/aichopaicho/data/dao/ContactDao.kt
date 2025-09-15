package com.aspiring_creators.aichopaicho.data.dao

import androidx.room.Query
import androidx.room.Upsert
import com.aspiring_creators.aichopaicho.data.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactDao {

    @Upsert
    suspend fun upsert(contact: Contact)

    @Query("UPDATE records SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT * FROM contacts WHERE isDeleted = 0")
    suspend fun getAllContacts(): Flow<List<Contact>>


}