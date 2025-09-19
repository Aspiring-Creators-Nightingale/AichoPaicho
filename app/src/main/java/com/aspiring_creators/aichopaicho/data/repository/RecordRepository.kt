package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.dao.RecordDao
import com.aspiring_creators.aichopaicho.data.entity.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordRepository @Inject constructor(private val recordDao: RecordDao) {

    fun getAllRecords(): Flow<List<Record>> {
        return recordDao.getAllRecords()
    }

    suspend fun upsert(record: Record) {
        recordDao.upsert(record)
    }

     fun getRecordsByContactId(contactId: String): Flow<List<Record>>{
        return recordDao.getRecordsByContactId(contactId)
    }

    suspend fun getTotalByType(typeId: Int): Int {
        return recordDao.getTotalByType(typeId)
    }

    // New methods needed for the screen
    fun getRecordsByDateRange(startDate: Long, endDate: Long): Flow<List<Record>> {
        return recordDao.getRecordsByDateRange(startDate, endDate)
    }

    suspend fun getRecordById(recordId: String): Record? {
        return recordDao.getRecordById(recordId)
    }

    suspend fun updateRecord(record: Record) {
        recordDao.updateRecord(record)
    }

    suspend fun deleteRecord(recordId: String) {
        recordDao.deleteRecord(recordId)
    }

    suspend fun insertRecord(record: Record) {
        recordDao.insertRecord(record)
    }
}