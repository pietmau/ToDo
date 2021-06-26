package com.pppp.todo.notification

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.pppp.todo.R
import com.pppp.todo.notification.WorkManagerNotificationScheduler.Companion.TEXT
import com.pppp.todo.toEpochMillis
import com.pppp.usecases.notification.NotificationScheduler
import com.pppp.usecases.notification.NotificationScheduler.Params
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

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
                        //      .putString(ID, params.id)
                        .build()
                )
                //.addTag(params.id)
                .build()
        )
    }

    private fun calculateDelay(timeInMills: Long) =
        timeInMills - LocalDateTime.now().toEpochMillis()

    internal companion object {
        internal const val TEXT = "text"
        internal const val ID = "id"
    }
}

class OneTimeScheduleWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val text = workerParams.inputData.getString(TEXT) ?: ""
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_checklist_24)
            .setContentTitle(text)//TODO
            .setContentText("Hello from one-time worker!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())// TODO
        }
        return Result.success()
    }

    private companion object {
        private const val CHANNEL_ID = "com.pppp.todo"
    }
}
