package com.pppp.usecases.todos

import com.pppp.entities.ToDo
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.ToDosRepository
import com.pppp.usecases.ToDosRepository.Params.GetSingle
import kotlinx.coroutines.flow.Flow

class GetToDoUseCase(private val toDosRepository: ToDosRepository) :
        FlowUseCase<List<ToDo>, GetToDoUseCase.Params> {

    override suspend fun invoke(params: Params): Flow<List<ToDo>> =
            when (params) {
                is Params.GetList -> toDosRepository.getList(params.userId, params.listId)
                is Params.GetSingleToDo -> toDosRepository.getToDo(GetSingle(
                        userId = params.userId,
                        listId = params.listId,
                        itemId = params.itemId,
                ))
            }

    sealed class Params {
        data class GetList(val userId: String, val listId: String) : Params()
        data class GetSingleToDo(val userId: String, val listId: String, val itemId: String) : Params()
    }
}
