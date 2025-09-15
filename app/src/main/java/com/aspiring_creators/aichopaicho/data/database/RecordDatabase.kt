package com.aspiring_creators.aichopaicho.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspiring_creators.aichopaicho.data.dao.RecordDao
import com.aspiring_creators.aichopaicho.data.dao.TypeDao
import com.aspiring_creators.aichopaicho.data.entity.Record
import com.aspiring_creators.aichopaicho.data.entity.Type

@Database(
    entities = [Record::class],
    version = 1
)
abstract class RecordDatabase: RoomDatabase() {

    abstract val dao: RecordDao

}