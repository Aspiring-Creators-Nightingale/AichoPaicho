package com.aspiring_creators.aichopaicho.data.dao

import androidx.room.Query
import androidx.room.Upsert
import com.aspiring_creators.aichopaicho.data.entity.Type

interface TypeDao {

    @Upsert
    suspend fun upsert(type: Type)

    @Query("UPDATE types SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT * FROM types WHERE isDeleted = 0")
    suspend fun getAllTypes(): List<Type>



}