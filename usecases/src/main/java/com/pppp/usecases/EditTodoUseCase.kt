package com.pppp.usecases

import com.pppp.usecases.Repository.Params.Add
import com.pppp.usecases.notification.NotificationScheduler

class EditTodoUseCase(
    private val repository: Repository,
    private val notificationScheduler: NotificationScheduler
) :
    UseCase<String, EditTodoUseCase.Params> {

    override suspend fun invoke(params: Params) =
        when (params) {
            is Params.Edit -> edit(params.id, params.values)
            is Params.Add -> add(params.title, params.due)
        }

    private suspend fun add(title: String, due: Long?): String {
        val id = repository.addToDo(Add(title = title, due = due))
        notificationScheduler.trySchedule(id, title, due)
        return id
    }

    private suspend fun edit(id: String, values: Map<String, Any?>): String {
        repository.edit(id, values)
        notificationScheduler.trySchedule(id, values)
        return id
    }

    sealed class Params {
        data class Edit(val id: String, val values: Map<String, Any?>) : Params()
        data class Add(val title: String, val due: Long? = null) : Params()
    }
}