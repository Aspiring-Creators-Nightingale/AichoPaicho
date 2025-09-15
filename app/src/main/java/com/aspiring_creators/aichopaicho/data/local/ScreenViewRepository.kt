package com.aspiring_creators.aichopaicho.data.repository

import com.aspiring_creators.aichopaicho.data.local.ScreenView
import com.aspiring_creators.aichopaicho.data.local.ScreenViewDao
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenViewRepository @Inject constructor(private val screenViewDao: ScreenViewDao) {

    suspend fun getScreenView(screenId: String): ScreenView? {
        return screenViewDao.getScreenView(screenId).firstOrNull() // Example: taking the first emission
    }

    suspend fun markScreenAsShown(screenId: String) {
        screenViewDao.upsertScreenView(ScreenView(screenId = screenId, hasBeenShown = true))
    }
}