package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.dao.ContactDao
import com.aspiring_creators.aichopaicho.data.entity.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(private val contactDao: ContactDao) {

    suspend fun upsert(contact: Contact) {
        contactDao.upsert(contact)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        contactDao.softDelete(id, updatedAt)
    }

    suspend fun getAllContacts(): Flow<List<Contact>> { // Changed from suspend fun based on typical DAO Flow usage
        return contactDao.getAllContacts()
    }
}