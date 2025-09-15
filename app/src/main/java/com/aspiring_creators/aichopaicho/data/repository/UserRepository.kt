package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.dao.UserDao
import com.aspiring_creators.aichopaicho.data.entity.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun upsert(user: User) {
        userDao.upsert(user)
    }

    suspend fun softDelete(id: String, updatedAt: Long) {
        userDao.softDelete(id, updatedAt)
    }

    suspend fun getUser(id: String): User? {
        return userDao.getUser(id)
    }
}