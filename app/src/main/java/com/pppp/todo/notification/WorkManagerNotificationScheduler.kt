package com.pppp.todo.notification

import androidx.work.*
import com.pppp.todo.toEpochMillis
import com.pppp.usecases.notification.NotificationScheduler
import com.pppp.usecases.notification.NotificationScheduler.Params
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerNotificationScheduler @Inject constructor(private val workManager: WorkManager) :
    NotificationScheduler {

    override fun schedule(params: Params) {
        val delay = calculateDelay(params.timeInMills)
        if (delay < 0) {
            return
        }
        workManager.enqueue(
            OneTimeWorkRequestBuilder<OneTimeScheduleWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    Data.Builder()
                        .putString(TEXT, params.text)
                        .putString(ID, params.id)
                        .putLong(TIME_DUE, params.timeInMills)
                        .build()
                )
                .addTag(params.id)
                .build()
        )
    }

    private fun calculateDelay(timeInMills: Long) =
        timeInMills - LocalDateTime.now().toEpochMillis()

    internal companion object {
        internal const val TEXT = "text"
        internal const val ID = "id"
        internal const val TIME_DUE = "time_due"
    }
}

