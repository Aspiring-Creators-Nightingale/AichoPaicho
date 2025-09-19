package com.aspiring_creators.aichopaicho.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aspiring_creators.aichopaicho.data.entity.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contact): Long

    @Query("SELECT * FROM contacts WHERE isDeleted = 0")
     fun getAllContacts(): Flow<List<Contact>>

     @Query("SELECT * FROM contacts WHERE contactId = :contactId")
    suspend fun getContactByContactId(contactId: String): Contact?



    @Query("SELECT * FROM contacts WHERE id = :contactId AND isDeleted = 0")
    suspend fun getContactById(contactId: String): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Query("UPDATE contacts SET isDeleted = 1, updatedAt = :timestamp WHERE id = :contactId")
    suspend fun deleteContact(contactId: String, timestamp: Long = System.currentTimeMillis())

}