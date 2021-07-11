package com.pppp.usecases.todos

import com.pppp.entities.ToDo
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.ToDosRepository
import kotlinx.coroutines.flow.Flow

class GetToDoUseCase(private val toDosRepository: ToDosRepository) :
    FlowUseCase<List<ToDo>, GetToDoUseCase.Params?> {

    override suspend fun invoke(params: Params?): Flow<List<ToDo>> =
        when (params) {
            is Params.GetAll -> toDosRepository.getToDo()
            is Params.GetSingle -> toDosRepository.getToDo(params.id)
            null -> toDosRepository.getToDo()
        }

    sealed class Params {
        object GetAll : Params()
        data class GetSingle(val id: String) : Params()
    }

}