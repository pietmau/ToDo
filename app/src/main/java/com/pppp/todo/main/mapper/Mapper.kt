package com.pppp.todo.main.mapper

import com.pppp.entities.ToDo
import com.pppp.todo.main.ToDoViewModel
import com.pppp.todo.main.TodoMainViewModel
import javax.inject.Inject

class Mapper @Inject constructor() : @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel {

    override fun invoke(toDos: List<ToDo>): TodoMainViewModel =
        TodoMainViewModel.Data(todos = toDos.map {
            ToDoViewModel(
                id = it.id,
                title = it.title,
                starred = it.starred
            )
        })
}