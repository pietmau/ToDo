package com.pppp.todo.notification

import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pppp.todo.calculateDelay
import com.pppp.usecases.notification.NotificationScheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerNotificationScheduler @Inject constructor(private val workManager: WorkManager) :
    NotificationScheduler {

    override fun trySchedule(id: String, text: String?, timeInMills: Long?) {
        removeScheduledIfAny(id)
        timeInMills ?: return
        text ?: return
        scheduleInternal(timeInMills.calculateDelay(), id, text, timeInMills)
    }

    override fun trySchedule(id: String, values: Map<String, Any?>) =
        trySchedule(id, values[TITLE] as? String?, values[DUE] as? Long?)

    private fun scheduleInternal(
        delay: Long,
        id: String,
        text: String?,
        timeInMills: Long
    ) {
        if (delay <= 0) {
            return
        }
        workManager.enqueueUniqueWork(
            id, REPLACE, OneTimeWorkRequestBuilder<OneTimeScheduleWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    Data.Builder()
                        .putString(TITLE, text)
                        .putString(ID, id)
                        .putLong(DUE, timeInMills)
                        .build()
                )
                .build()
        )
    }

    private fun removeScheduledIfAny(id: String?) = id?.let { workManager.cancelUniqueWork(id) }

    companion object {
        const val ID = "id"
        const val DUE = "due"
        const val TITLE = "due"
    }
}

