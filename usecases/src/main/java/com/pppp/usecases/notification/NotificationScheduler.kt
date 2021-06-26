package com.pppp.usecases.notification

interface NotificationScheduler {

    fun schedule(params: Params)

    data class Params(
        val text: String,
        val timeInMills: Long,
    )
}