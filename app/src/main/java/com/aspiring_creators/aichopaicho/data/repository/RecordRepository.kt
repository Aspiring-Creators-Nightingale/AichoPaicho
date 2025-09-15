package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.database.RecordDatabase
import com.aspiring_creators.aichopaicho.data.entity.Record
import kotlinx.coroutines.flow.Flow

class RecordRepository(private val db: RecordDatabase) {

    private val dao = db.dao

    fun getAllRecords(): Flow<List<Record>> {
        return dao.getAllRecords()
    }

    suspend fun upsert(record: Record) {
        dao.upsert(record)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        dao.softDelete(id, updatedAt)
    }

    suspend fun getTotalByType(typeId: Int): Int {
        return dao.getTotalByType(typeId)
    }
}