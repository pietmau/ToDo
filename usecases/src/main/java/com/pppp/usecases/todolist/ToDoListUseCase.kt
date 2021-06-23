package com.pppp.usecases.todolist

import com.pppp.usecases.Repository
import com.pppp.usecases.UseCase
import com.sun.tools.javac.comp.Todo

class ToDoListUseCase(private val repository: Repository) : UseCase<Result<List<Todo>>, Nothing>() {

    override suspend fun invoke(params: Nothing?): Result<List<Todo>> =
        try {
            Result.success(repository.getToDos())
        } catch (exception: Exception) {
            Result.failure(exception)
        }
}