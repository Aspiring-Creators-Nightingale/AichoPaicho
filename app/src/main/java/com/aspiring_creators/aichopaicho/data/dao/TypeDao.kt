package com.aspiring_creators.aichopaicho.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.aspiring_creators.aichopaicho.data.entity.Type

@Dao
interface TypeDao {

    @Upsert
    suspend fun upsert(type: Type)

    @Insert
    suspend fun insertAll(types: List<Type>)

    @Query("UPDATE types SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT * FROM types WHERE isDeleted = 0")
    suspend fun getAllTypes(): List<Type>

    @Query("SELECT * FROM types WHERE name = :name")
    suspend fun getByName(name: String): Type

}