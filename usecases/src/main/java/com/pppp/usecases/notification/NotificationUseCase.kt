package com.pppp.usecases.notification

import com.pppp.usecases.UseCase

class NotificationUseCase(private val notificationScheduler: NotificationScheduler) :
    UseCase<Unit, NotificationUseCase.Params> {

    override suspend fun invoke(params: Params) {
        params.timeInMills?.let {
            notificationScheduler.schedule(
                NotificationScheduler.Params(
                    text = params.text,
                    timeInMills = it
                )
            )
        }
    }

    data class Params(
        val text: String,
        val timeInMills: Long? = null,
    )
}