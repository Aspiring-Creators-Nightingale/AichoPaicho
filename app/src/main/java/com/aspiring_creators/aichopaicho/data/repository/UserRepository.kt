package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.database.UserDatabase
import com.aspiring_creators.aichopaicho.data.entity.User

class UserRepository(private val db: UserDatabase) {

    private val dao = db.dao

    suspend fun upsert(user: User) {
        dao.upsert(user)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        dao.softDelete(id, updatedAt)
    }

    suspend fun getUser(id: String): User? { // Assuming User? as return type
        return dao.getUser(id)
    }
}