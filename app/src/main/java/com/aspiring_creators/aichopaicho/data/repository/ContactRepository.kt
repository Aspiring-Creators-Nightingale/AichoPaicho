package com.aspiring_creators.aichopaicho.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.aspiring_creators.aichopaicho.data.dao.ContactDao
import com.aspiring_creators.aichopaicho.data.entity.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(private val contactDao: ContactDao) {

    suspend fun checkAndInsert(contact: Contact) : Boolean {
        return try {
            contactDao.insert(contact)
            true
        } catch (e: SQLiteConstraintException) {
            false
            throw SQLiteConstraintException("Contact with the same contactId already exists.")
        } catch (e: Exception) {

            false
        }
    }


     suspend fun getContactByContactId(contactId: String): Contact? {
        return contactDao.getContactByContactId(contactId)
    }

    fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }

    suspend fun getContactById(contactId: String): Contact? {
        return contactDao.getContactById(contactId)
    }

    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactDao.updateContact(contact)
    }

    suspend fun deleteContact(contactId: String) {
        contactDao.deleteContact(contactId)
    }

}