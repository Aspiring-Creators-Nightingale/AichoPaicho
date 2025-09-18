package com.aspiring_creators.aichopaicho.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aspiring_creators.aichopaicho.data.entity.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contact): Long

    @Query("UPDATE records SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT * FROM contacts WHERE isDeleted = 0")
     fun getAllContacts(): Flow<List<Contact>>

     @Query("SELECT * FROM contacts WHERE contactId = :contactId")
    suspend fun getContactByContactId(contactId: String): Contact?

}