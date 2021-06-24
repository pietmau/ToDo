package com.pppp.usecases.todolist

import com.pppp.entities.ToDo
import com.pppp.usecases.Repository
import com.pppp.usecases.UseCase

class ToDoListUseCase constructor(private val repository: Repository) :
    UseCase<Result<List<ToDo>>, Nothing>() {

    override suspend fun invoke(params: Nothing?): Result<List<ToDo>> =
        try {
            Result.success(repository.getToDos())
        } catch (exception: Exception) {
            Result.failure(exception)
        }
}