package com.pppp.usecases.todos

import com.pppp.usecases.ToDosRepository
import com.pppp.usecases.ToDosRepository.Params.Add
import com.pppp.usecases.UseCase
import com.pppp.usecases.notification.NotificationScheduler

class EditTodoUseCase(
    private val toDosRepository: ToDosRepository,
    private val notificationScheduler: NotificationScheduler,
    private val userId: String
) :
    UseCase<String, EditTodoUseCase.Params> {

    override suspend fun invoke(params: Params) =
        when (params) {
            is Params.Edit -> edit(params.listId, params.itemId, params.values)
            is Params.Add -> add(params.listId, params.title, params.due)
        }

    private suspend fun add(listId: String, title: String, due: Long?): String {
        val itemId =
            toDosRepository.addToDo(Add(userId = userId, listId = listId, title = title, due = due))
        notificationScheduler.trySchedule(itemId, title, due)
        return itemId
    }

    private suspend fun edit(
        listId: String,
        itemId: String,
        values: Map<String, Any?>
    ): String {
        toDosRepository.edit(
            ToDosRepository.Params.Edit(
                userId = userId,
                listId = listId,
                itemId = itemId,
                values = values
            )
        )
        notificationScheduler.trySchedule(itemId, values)
        return itemId
    }

    sealed class Params {
        data class Edit(val listId: String, val itemId: String, val values: Map<String, Any?>) :
            Params()

        data class Add(val listId: String, val title: String, val due: Long? = null) : Params()
    }
}