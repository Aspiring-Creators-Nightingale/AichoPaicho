package com.aspiring_creators.aichopaicho.data

import android.net.Uri
import androidx.room.TypeConverter
import androidx.core.net.toUri
import java.util.Date

class Converters {

    @TypeConverter
    fun fromUri(uri : Uri?): String? {
        return uri?.toString() ?: ""
    }

    @TypeConverter
    fun stringToUri(value: String?): Uri?{
        return value?.toUri()
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}