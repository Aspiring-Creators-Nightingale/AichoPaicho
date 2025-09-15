package com.aspiring_creators.aichopaicho.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreenViewDao {

    @Upsert
    suspend fun upsertScreenView(screenView: ScreenView)

    @Query("SELECT * FROM screen_views WHERE screenId = :screenId")
    fun getScreenView(screenId: String): Flow<ScreenView?>
}