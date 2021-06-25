package com.pppp.todo.main.mapper

import android.util.Log
import com.pppp.entities.ToDo
import com.pppp.todo.main.ErrorMessage
import com.pppp.todo.main.ToDoViewModel
import com.pppp.todo.main.TodoMainViewModel
import javax.inject.Inject

class Mapper @Inject constructor() : @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel {

    override fun invoke(result: List<ToDo>): TodoMainViewModel =
        TodoMainViewModel(todos = (result ?: emptyList()).map {
            ToDoViewModel(
                id = it.id!!,
                title = it.title,
                starred = it.starred
            )
        })
}

