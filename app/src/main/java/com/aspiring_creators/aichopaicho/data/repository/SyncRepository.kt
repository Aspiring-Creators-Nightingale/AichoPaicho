package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.Timestamp // Import Timestamp
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

@Singleton
class SyncRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    private val recordRepository: RecordRepository,
    private val firebaseAuth: FirebaseAuth,
) {

    suspend fun syncContacts() {
        try {
            val contacts = contactRepository.getAllContacts()
            val user = userRepository.getUser()

            val listOfContacts = contacts.first()
            listOfContacts.forEach { contact ->
                try {
                    val contactData = hashMapOf(
                        "id" to contact.id,
                        "name" to contact.name,
                        "phone" to contact.phone,
                        "userId" to contact.userId,
                        "contactId" to contact.contactId,
                        "deleted" to contact.isDeleted, // Matches @PropertyName
                        "createdAt" to contact.createdAt,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                    firestore.collection("users")
                        .document(user.id)
                        .collection("contacts")
                        .document(contact.id)
                        .set(contactData, SetOptions.merge())
                        .await()
                } catch (e: Exception) {
                    println("Error syncing contact ${contact.id}: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("Error fetching contacts for sync: ${e.message}")
        }
    }

    suspend fun syncSingleContact(contactId: String) {
        try {
            val contact = contactRepository.getContactById(contactId)
            val user = userRepository.getUser()
            try {
                contact?.let { c ->
                    val contactData = hashMapOf(
                        "id" to c.id,
                        "name" to c.name,
                        "phone" to c.phone,
                        "userId" to c.userId,
                        "contactId" to c.contactId,
                        "deleted" to c.isDeleted, // Matches @PropertyName
                        "createdAt" to c.createdAt,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                    firestore.collection("users")
                        .document(user.id)
                        .collection("contacts")
                        .document(c.id)
                        .set(contactData, SetOptions.merge())
                        .await()
                }
            } catch (e: Exception) {
                println("Error syncing contact ${contact?.id}: ${e.message}")
            }
        } catch (e: Exception) {
            println("Error fetching contacts for sync: ${e.message}")
        }
    }

    suspend fun syncRecords() {
        try {
            val records = recordRepository.getAllRecords()
            val user = userRepository.getUser()

            val  listOfRecords = records.first()
            listOfRecords.forEach { record ->
                try {
                    val recordData = hashMapOf(
                        "id" to record.id,
                        "userId" to record.userId,
                        "contactId" to record.contactId,
                        "date" to record.date,
                        "deleted" to record.isDeleted, // Matches @PropertyName
                        "complete" to record.isComplete, // Matches @PropertyName
                        "description" to record.description,
                        "typeId" to record.typeId,
                        "amount" to record.amount,
                        "createdAt" to record.createdAt,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                    firestore.collection("users")
                        .document(user.id)
                        .collection("records")
                        .document(record.id)
                        .set(recordData, SetOptions.merge())
                        .await()
                } catch (e: Exception) {
                    println("Error syncing record ${record.id}: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("Error fetching records for sync: ${e.message}")
        }
    }

    suspend fun syncSingleRecord(recordId: String) {
        try {
            val record = recordRepository.getRecordById(recordId)
            val user = userRepository.getUser()

            try {
                record?.let { r ->
                    val recordData = hashMapOf(
                        "id" to r.id,
                        "userId" to r.userId,
                        "contactId" to r.contactId,
                        "date" to r.date,
                        "deleted" to r.isDeleted, // Matches @PropertyName
                        "complete" to r.isComplete, // Matches @PropertyName
                        "description" to r.description,
                        "typeId" to r.typeId,
                        "amount" to r.amount,
                        "createdAt" to r.createdAt,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                    firestore.collection("users")
                        .document(user.id)
                        .collection("records")
                        .document(r.id)
                        .set(recordData, SetOptions.merge())
                        .await()
                }
            } catch (e: Exception) {
                println("Error syncing record ${record?.id}: ${e.message}")
            }
        } catch (e: Exception) {
            println("Error fetching records for sync: ${e.message}")
        }
    }

    suspend fun syncUserData() {
        try {
            val user = userRepository.getUser()
            firestore.collection("users")
                .document(user.id)
                .set(user, SetOptions.merge()) 
                .await()
        } catch (e: Exception) {
            println("Error syncing user data: ${e.message}")
        }
    }

    suspend fun downloadAndMergeData() {
        try {
            val user = userRepository.getUser()
            if (user.id.isBlank()) {
                println("User ID is blank. Skipping download.")
                return
            } else {
                if (firebaseAuth.currentUser?.uid != user.id) {
                    println("Current Firebase Auth user does not match local user. Skipping download.")
                    return
                }
            }

            // Download contacts
            try {
                val contactsSnapshot = firestore.collection("users")
                    .document(user.id)
                    .collection("contacts")
                    .get()
                    .await()

                for (doc in contactsSnapshot.documents) {
                    try {
                        val firestoreId = doc.getString("id") ?: ""
                        val firestoreName = doc.getString("name") ?: ""
                        val firestoreUserId = doc.getString("userId")
                        @Suppress("UNCHECKED_CAST")
                        val firestorePhone = doc.get("phone") as? List<String?> ?: emptyList()
                        val firestoreContactId = doc.getString("contactId")
                        val firestoreIsDeleted = doc.getBoolean("deleted") ?: false
                        val firestoreCreatedAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        val firestoreUpdatedAtTimestamp = doc.getTimestamp("updatedAt")
                        val firestoreUpdatedAt = firestoreUpdatedAtTimestamp?.toDate()?.time ?: System.currentTimeMillis()

                        if (firestoreId.isEmpty()) {
                            println("Parsed contact from Firestore with empty ID: ${doc.id}, skipping.")
                            continue
                        }

                        val firestoreContact = Contact(
                            id = firestoreId,
                            name = firestoreName,
                            userId = firestoreUserId,
                            phone = firestorePhone,
                            contactId = firestoreContactId,
                            isDeleted = firestoreIsDeleted,
                            createdAt = firestoreCreatedAt,
                            updatedAt = firestoreUpdatedAt
                        )

                        val localContact = contactRepository.getContactById(firestoreContact.id)
                        if (localContact == null) {
                            contactRepository.insertContact(firestoreContact)
                            println("Inserted new contact from Firestore: ${firestoreContact.id}")
                        } else {
                            if (firestoreContact.updatedAt > localContact.updatedAt) {
                                contactRepository.updateContact(firestoreContact)
                                println("Updated local contact from Firestore: ${firestoreContact.id}")
                            } else if (localContact.updatedAt > firestoreContact.updatedAt) {
                                println("Local contact ${localContact.id} is newer. Re-uploading.")
                                syncSingleContact(localContact.id)
                            } else {
                                println("Contact ${localContact.id} timestamps match or already synced.")
                            }
                        }
                    } catch (e: Exception) {
                         println("Error processing contact document ${doc.id}: ${e.message}")
                         e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                println("Error downloading contacts: ${e.message}")
                e.printStackTrace()
            }

            // Download records
            try {
                val recordsSnapshot = firestore.collection("users")
                    .document(user.id)
                    .collection("records")
                    .get()
                    .await()

                for (doc in recordsSnapshot.documents) {
                   try {
                        val firestoreId = doc.getString("id") ?: ""
                        val firestoreUserId = doc.getString("userId")
                        val firestoreContactId = doc.getString("contactId")
                        val firestoreTypeId = doc.getLong("typeId")?.toInt() ?: 0
                        val firestoreAmount = doc.getLong("amount")?.toInt() ?: 0
                        val firestoreDate = doc.getLong("date") ?: 0L
                        val firestoreIsComplete = doc.getBoolean("complete") ?: false
                        val firestoreIsDeleted = doc.getBoolean("deleted") ?: false
                        val firestoreDescription = doc.getString("description")
                        val firestoreCreatedAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        val firestoreUpdatedAtTimestamp = doc.getTimestamp("updatedAt")
                        val firestoreUpdatedAt = firestoreUpdatedAtTimestamp?.toDate()?.time ?: System.currentTimeMillis()

                        if (firestoreId.isEmpty()) {
                            println("Parsed record from Firestore with empty ID: ${doc.id}, skipping.")
                            continue
                        }

                        val firestoreRecord = Record(
                            id = firestoreId,
                            userId = firestoreUserId,
                            contactId = firestoreContactId,
                            typeId = firestoreTypeId,
                            amount = firestoreAmount,
                            date = firestoreDate,
                            isComplete = firestoreIsComplete,
                            isDeleted = firestoreIsDeleted,
                            description = firestoreDescription,
                            createdAt = firestoreCreatedAt,
                            updatedAt = firestoreUpdatedAt
                        )

                        val localRecord = recordRepository.getRecordById(firestoreRecord.id)
                        if (localRecord == null) {
                            recordRepository.insertRecord(firestoreRecord)
                            println("Inserted new record from Firestore: ${firestoreRecord.id}")
                        } else {
                            if (firestoreRecord.updatedAt > localRecord.updatedAt) {
                                recordRepository.updateRecord(firestoreRecord)
                                println("Updated local record from Firestore: ${firestoreRecord.id}")
                            } else if (localRecord.updatedAt > firestoreRecord.updatedAt) {
                                println("Local record ${localRecord.id} is newer. Re-uploading.")
                                syncSingleRecord(localRecord.id)
                            } else {
                                println("Record ${localRecord.id} timestamps match or already synced.")
                            }
                        }
                    } catch (e: Exception) {
                         println("Error processing record document ${doc.id}: ${e.message}")
                         e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                println("Error downloading records: ${e.message}")
                e.printStackTrace()
            }
        } catch (e: Exception) {
            println("Error in downloadAndMergeData: ${e.message}")
            e.printStackTrace()
        }
    }
}
