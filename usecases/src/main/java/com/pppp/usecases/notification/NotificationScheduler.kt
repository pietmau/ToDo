package com.pppp.usecases.notification

interface NotificationScheduler {

    fun trySchedule(id: String, text: String?, timeInMills: Long?)

    fun trySchedule(id: String, values: Map<String, Any?>)

}