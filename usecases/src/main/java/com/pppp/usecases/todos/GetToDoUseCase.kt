package com.pppp.usecases.todos

import com.pppp.entities.ToDo
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.ToDosRepository
import kotlinx.coroutines.flow.Flow

class GetToDoUseCase(private val toDosRepository: ToDosRepository) :
        FlowUseCase<List<ToDo>, GetToDoUseCase.Params> {

    override suspend fun invoke(params: Params): Flow<List<ToDo>> =
            when (params) {
                is Params.GetList -> toDosRepository.getList(params.userId, params.doList)
                is Params.GetSingleToDo -> toDosRepository.getToDo(params.id)
            }

    sealed class Params {
        data class GetList(val userId: String, val doList: String) : Params()
        data class GetSingleToDo(val id: String) : Params()
    }
}