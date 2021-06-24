package com.pppp.todo.main.mapper

import android.util.Log
import com.pppp.entities.ToDo
import com.pppp.todo.main.ErrorMessage
import com.pppp.todo.main.ToDoViewModel
import com.pppp.todo.main.TodoMainViewModel
import javax.inject.Inject

class Mapper @Inject constructor() :
    @JvmSuppressWildcards (Result<List<ToDo>>) -> TodoMainViewModel {

    override fun invoke(result: Result<List<ToDo>>): TodoMainViewModel =
        if (result.isSuccess) {
            TodoMainViewModel(todos = (result.getOrNull() ?: emptyList()).map {
                ToDoViewModel(
                    id = it.id,
                    title = it.title,
                    starred = it.starred
                )
            })
        } else {
            TodoMainViewModel(error = ErrorMessage(result.exceptionOrNull()?.message ?: ERROR))
        }

    private companion object {
        private const val ERROR = "Error"
    }
}

