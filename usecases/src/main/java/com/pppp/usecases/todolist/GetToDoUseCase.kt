package com.pppp.usecases.todolist

import com.pppp.entities.ToDo
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.Repository
import kotlinx.coroutines.flow.Flow

class GetToDoUseCase(private val repository: Repository) :
    FlowUseCase<List<ToDo>, GetToDoUseCase.Params?> {

    override suspend fun invoke(params: Params?): Flow<List<ToDo>> =
        when (params) {
            is Params.GetAll -> repository.getToDo()
            is Params.GetSingle -> repository.getToDo(params.id)
            null -> repository.getToDo()
        }

    sealed class Params {
        object GetAll : Params()
        data class GetSingle(val id: String) : Params()
    }

}