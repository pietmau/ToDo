package com.pppp.todo.main.mapper

import com.pppp.entities.ToDo
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.toDoViewModel
import javax.inject.Inject

class Mapper @Inject constructor() : @JvmSuppressWildcards (List<ToDo>) -> MainViewState {

    override fun invoke(result: List<ToDo>): MainViewState =
        MainViewState(todos = result.map {
            it.toDoViewModel()
        })

}

