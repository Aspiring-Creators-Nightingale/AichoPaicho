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

    suspend fun softDelete(id: String, updatedAt: Long) {
        recordDao.softDelete(id, updatedAt)
    }

    suspend fun getTotalByType(typeId: Int): Int {
        return recordDao.getTotalByType(typeId)
    }
}