package com.pppp.todo.notification

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pppp.todo.R
import com.pppp.todo.main.MainActivity
import com.pppp.todo.main.MainActivity.Companion.PENDING_INTENT_REQUEST_CODE
import com.pppp.todo.notification.WorkManagerNotificationScheduler.Companion.DUE
import com.pppp.todo.notification.WorkManagerNotificationScheduler.Companion.TITLE
import com.pppp.todo.toDueDateNotificationText
import com.pppp.uielements.toMillis
import java.time.LocalDateTime.now


class OneTimeScheduleWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            getActivity(
                context,
                PENDING_INTENT_REQUEST_CODE,
                intent,
                FLAG_UPDATE_CURRENT
            )
        val text = workerParams.inputData.getString(TITLE)
        val title = "TODO at " + workerParams.inputData.getLong(DUE, now().toMillis())
            .toDueDateNotificationText()
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_baseline_checklist_24)
            .setContentTitle(title)
            .setAutoCancel(false)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setPriority(PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            createNotificationChannel(builder)
            notify(NOTIFICATION_ID, builder.build())
        }
        return Result.success()
    }

    private fun NotificationManagerCompat.createNotificationChannel(builder: NotificationCompat.Builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            }
            builder.setChannelId(CHANNEL_ID)
            createNotificationChannel(notificationChannel);
        }
    }

    private companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "com.pppp.todo"
        private const val CHANNEL_NAME = "com.pppp.todo.notifications"
    }
}