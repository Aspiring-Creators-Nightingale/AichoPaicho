package com.aspiring_creators.aichopaicho.data

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkerUtils {

    fun enqueueOneTimeSync(context: Context) {
        val appContext = context.applicationContext

        val workRequest = OneTimeWorkRequestBuilder<BackgroundSyncWorker>()
            .addTag(BackgroundSyncWorker.ONE_TIME_SYNC_WORK_NAME)
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            BackgroundSyncWorker.ONE_TIME_SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        Log.d("WorkerUtils", "One-time sync enqueued")
    }

    fun enqueuePeriodicSync(context: Context) {
        val appContext = context.applicationContext

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<BackgroundSyncWorker>(
            24, TimeUnit.HOURS,
            2, TimeUnit.HOURS // flex
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            BackgroundSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )

        Log.d("WorkerUtils", "Periodic sync enqueued")
    }

    fun cancelSync(context: Context) {
        val appContext = context.applicationContext
        WorkManager.getInstance(appContext).cancelUniqueWork(BackgroundSyncWorker.WORK_NAME)
        WorkManager.getInstance(appContext).cancelUniqueWork(BackgroundSyncWorker.ONE_TIME_SYNC_WORK_NAME)
    }
}
