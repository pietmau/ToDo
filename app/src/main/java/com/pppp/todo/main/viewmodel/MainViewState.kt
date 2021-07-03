package com.pppp.todo.main.viewmodel

data class MainViewState(
    val isLoading: Boolean = true,
    val todos: List<ToDoViewModel> = emptyList(),
    val error: ErrorMessage? = null,
    val isAddTodoShowing: Boolean = false,
    val toDoBeingEdited: ToDoViewModel? = null
)


data class ToDoViewModel(
    val id: String,
    val title: String,
    val starred: Boolean = false,
    val due: Long? = null
)

data class ErrorMessage(val message: String)