package com.aspiring_creators.aichopaicho.di

import android.content.Context
import androidx.room.Room
import com.aspiring_creators.aichopaicho.data.dao.ContactDao
import com.aspiring_creators.aichopaicho.data.dao.RecordDao
import com.aspiring_creators.aichopaicho.data.dao.TypeDao
import com.aspiring_creators.aichopaicho.data.dao.UserDao
import com.aspiring_creators.aichopaicho.data.database.ContactDatabase
import com.aspiring_creators.aichopaicho.data.database.RecordDatabase
import com.aspiring_creators.aichopaicho.data.database.TypeDatabase
import com.aspiring_creators.aichopaicho.data.database.UserDatabase
import com.aspiring_creators.aichopaicho.data.local.ScreenViewDao
import com.aspiring_creators.aichopaicho.data.local.ScreenViewDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContactDatabase(@ApplicationContext appContext: Context): ContactDatabase {
        return Room.databaseBuilder(
            appContext,
            ContactDatabase::class.java,
            "contact_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.dao // Assuming 'val dao: ContactDao' in ContactDatabase
    }

    @Provides
    @Singleton
    fun provideTypeDatabase(@ApplicationContext appContext: Context): TypeDatabase {
        return Room.databaseBuilder(
            appContext,
            TypeDatabase::class.java,
            "type_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTypeDao(typeDatabase: TypeDatabase): TypeDao {
        return typeDatabase.dao // Assuming 'val dao: TypeDao' in TypeDatabase
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.dao // Assuming 'val dao: UserDao' in UserDatabase
    }

    @Provides
    @Singleton
    fun provideRecordDatabase(@ApplicationContext appContext: Context): RecordDatabase {
        return Room.databaseBuilder(
            appContext,
            RecordDatabase::class.java,
            "record_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecordDao(recordDatabase: RecordDatabase): RecordDao {
        return recordDatabase.dao // Assuming 'val dao: RecordDao' in RecordDatabase
    }

    @Provides
    @Singleton
    fun provideScreenViewDatabase(@ApplicationContext appContext: Context): ScreenViewDatabase {
        return Room.databaseBuilder(
                appContext,
                ScreenViewDatabase::class.java,
                "screen_view_database"
            ).fallbackToDestructiveMigration(false) // Added fallback for new DB, adjust as needed
         .build()
    }

    @Provides
    @Singleton
    fun provideScreenViewDao(screenViewDatabase: ScreenViewDatabase): ScreenViewDao {
        return screenViewDatabase.screenViewDao() // Assuming 'fun screenViewDao(): ScreenViewDao'
    }
}