package com.pppp.todo.main.mapper

import com.pppp.entities.ToDo
import com.pppp.todo.main.ToDoViewModel
import com.pppp.todo.main.TodoMainViewModel
import com.pppp.todo.toDoViewModel
import javax.inject.Inject

class Mapper @Inject constructor() : @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel {

    override fun invoke(result: List<ToDo>): TodoMainViewModel =
        TodoMainViewModel(todos = result.map {
            it.toDoViewModel()
        })

}

