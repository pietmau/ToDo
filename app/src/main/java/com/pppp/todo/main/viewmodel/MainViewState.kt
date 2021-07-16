package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo

data class MainViewState(
        val isLoading: Boolean = true,
        val todos: List<ToDoViewModel> = emptyList(),
        val error: ErrorMessage? = null,
        val itemBeingEdited: ToDo? = null,
        val addToDo: AddToDo = AddToDo.Hidden
)

sealed class AddToDo {
    object Hidden : AddToDo()
    object Showing : AddToDo()
}

data class ToDoViewModel(
        val id: String,
        val title: String,
        val starred: Boolean = false,
        val due: Long? = null
)

data class ErrorMessage(val message: String)