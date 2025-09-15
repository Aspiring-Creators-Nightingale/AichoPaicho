package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.database.TypeDatabase
import com.aspiring_creators.aichopaicho.data.entity.Type

class TypeRepository(private val db: TypeDatabase) {

    private val dao = db.dao

    suspend fun upsert(type: Type) {
        dao.upsert(type)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        dao.softDelete(id, updatedAt)
    }

    suspend fun getAllTypes(): List<Type> {
        return dao.getAllTypes()
    }
}