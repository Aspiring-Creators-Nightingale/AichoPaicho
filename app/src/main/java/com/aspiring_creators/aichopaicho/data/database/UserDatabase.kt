package com.aspiring_creators.aichopaicho.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspiring_creators.aichopaicho.data.dao.TypeDao
import com.aspiring_creators.aichopaicho.data.dao.UserDao
import com.aspiring_creators.aichopaicho.data.entity.Type
import com.aspiring_creators.aichopaicho.data.entity.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {

    abstract val dao: UserDao

}