package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.database.ContactDatabase
import com.aspiring_creators.aichopaicho.data.entity.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val db: ContactDatabase) {

    private val dao = db.dao // Changed from db.contactDao()

    suspend fun upsert(contact: Contact) {
        dao.upsert(contact)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        dao.softDelete(id, updatedAt)
    }

    suspend fun getAllContacts(): Flow<List<Contact>> {
        return dao.getAllContacts()
    }
}