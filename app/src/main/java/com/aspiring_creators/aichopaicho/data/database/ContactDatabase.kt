package com.aspiring_creators.aichopaicho.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspiring_creators.aichopaicho.data.dao.ContactDao
import com.aspiring_creators.aichopaicho.data.dao.TypeDao
import com.aspiring_creators.aichopaicho.data.entity.Contact
import com.aspiring_creators.aichopaicho.data.entity.Type

@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactDatabase: RoomDatabase() {

    abstract val dao: ContactDao

}