package com.aspiring_creators.aichopaicho.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspiring_creators.aichopaicho.data.dao.TypeDao
import com.aspiring_creators.aichopaicho.data.entity.Type

@Database(
    entities = [Type::class],
    version = 1
)
abstract class TypeDatabase: RoomDatabase() {

    abstract val dao: TypeDao

}