package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.dao.TypeDao
import com.aspiring_creators.aichopaicho.data.entity.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeRepository @Inject constructor(private val typeDao: TypeDao) {

    suspend fun upsert(type: Type) {
        typeDao.upsert(type)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        typeDao.softDelete(id, updatedAt)
    }

    suspend fun getAllTypes(): List<Type> {
        return typeDao.getAllTypes()
    }
}