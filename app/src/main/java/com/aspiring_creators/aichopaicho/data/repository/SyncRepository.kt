package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
                            "contactId" to contact.id,
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
                    contact?.let { contact ->
                        val contactData = hashMapOf(
                            "id" to contact.id,
                            "name" to contact.name,
                            "phone" to contact.phone,
                            "userId" to contact.userId,
                            "contactId" to contact.id,
                            "updatedAt" to FieldValue.serverTimestamp()
                        )

                        firestore.collection("users")
                            .document(user.id)
                            .collection("contacts")
                            .document(contact.id)
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
                            "isDeleted" to record.isDeleted,
                            "isComplete" to record.isComplete,
                            "description" to record.description,
                            "typeId" to record.typeId,
                            "amount" to record.amount,
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
                    record?.let { record ->
                        val recordData = hashMapOf(
                            "id" to record.id,
                            "userId" to record.userId,
                            "contactId" to record.contactId,
                            "date" to record.date,
                            "isDeleted" to record.isDeleted,
                            "isComplete" to record.isComplete,
                            "description" to record.description,
                            "typeId" to record.typeId,
                            "amount" to record.amount,
                            "updatedAt" to FieldValue.serverTimestamp()

                        )

                        firestore.collection("users")
                            .document(user.id)
                            .collection("records")
                            .document(record.id)
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
                .set(user)
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
            }else{
                if(firebaseAuth.currentUser?.uid != user.id){
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
                    val firestoreContact = doc.toObject(Contact::class.java)
                    if (firestoreContact == null) {
                        println("Failed to parse contact from Firestore: ${doc.id}")
                        continue
                    }
                    val localContact = contactRepository.getContactById(firestoreContact.id)
                    if (localContact == null) {
                        // New contact from Firestore, insert it locally
                        contactRepository.insertContact(firestoreContact)
                        println("Inserted new contact from Firestore: ${firestoreContact.id}")
                    }else{
                        val firestoreLastUpdatedMillis = getMillisFromTimestamp(firestoreContact.updatedAt) // Helper needed
                        val localLastUpdatedMillis = localContact.updatedAt

                        if (firestoreLastUpdatedMillis > localLastUpdatedMillis) {
                            // Firestore version is newer, update local
                            contactRepository.updateContact(firestoreContact)
                            println("Updated local contact from Firestore: ${firestoreContact.id}")
                        } else if (localLastUpdatedMillis > firestoreLastUpdatedMillis) {
                            println("Local contact ${localContact.id} is newer. Consider re-uploading.")
                            syncSingleContact(localContact.id)
                        } else {
                            // Timestamps are the same, no action needed or they are already in sync.
                            println("Contact ${localContact.id} timestamps match or already synced.")
                        }
                    }
                 }

            } catch (e: Exception) {
                println("Error downloading contacts: ${e.message}")
            }

            // Download records
            try {
                val recordsSnapshot = firestore.collection("users")
                    .document(user.id)
                    .collection("records")
                    .get()
                    .await()

               for(doc in recordsSnapshot.documents){
                    val firestoreRecord = doc.toObject(Record::class.java)

                    if (firestoreRecord == null) {
                        println("Failed to parse record from Firestore: ${doc.id}")
                        continue
                    }

                    val localRecord = recordRepository.getRecordById(firestoreRecord.id)
                    if (localRecord == null) {
                        // New record from Firestore, insert it locally
                        recordRepository.insertRecord(firestoreRecord)
                        println("Inserted new record from Firestore: ${firestoreRecord.id}")
                    }else{
                        val firestoreLastUpdatedMillis = getMillisFromTimestamp(firestoreRecord.updatedAt)
                        val localLastUpdatedMillis = localRecord.updatedAt
                        if (firestoreLastUpdatedMillis > localLastUpdatedMillis) {
                            // Firestore version is newer, update local
                            recordRepository.updateRecord(firestoreRecord)
                            println("Updated local record from Firestore: ${firestoreRecord.id}")
                        } else if (localLastUpdatedMillis > firestoreLastUpdatedMillis) {
                            println("Local record ${localRecord.id} is newer. Consider re-uploading.")
                            syncSingleRecord(localRecord.id)
                        }
                        else {
                            // Timestamps are the same, no action needed or they are already in sync.
                            println("Record ${localRecord.id} timestamps match or already synced.")
                        }
                    }

                }
            } catch (e: Exception) {
                println("Error downloading records: ${e.message}")
            }

        } catch (e: Exception) {
            println("Error downloading user data: ${e.message}")
        }
    }
    private fun getMillisFromTimestamp(timestamp: Any?): Long {
        return when (timestamp) {
            is com.google.firebase.Timestamp -> timestamp.toDate().time
            is Long -> timestamp
            else -> 0L
        }
    }

}
