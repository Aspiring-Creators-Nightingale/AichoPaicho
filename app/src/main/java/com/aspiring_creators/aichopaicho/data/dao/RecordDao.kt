package com.aspiring_creators.aichopaicho.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.aspiring_creators.aichopaicho.data.entity.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM records WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllRecords(): Flow<List<Record>>

    @Upsert
    suspend fun upsert(record: Record)

    @Query("UPDATE records SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT SUM(amount) FROM records WHERE typeId = :typeId AND isDeleted = 0")
    suspend fun getTotalByType(typeId: Int): Int
}
