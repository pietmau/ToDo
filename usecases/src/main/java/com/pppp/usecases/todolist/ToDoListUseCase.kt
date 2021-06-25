package com.pppp.usecases.todolist

import com.pppp.entities.ToDo
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.Repository
import kotlinx.coroutines.flow.Flow

class ToDoListUseCase(private val repository: Repository) : FlowUseCase<List<ToDo>, Nothing?> {

    override suspend fun invoke(params: Nothing?): Flow<List<ToDo>> = repository.getToDos()

}