package com.pppp.todo.main

data class TodoMainViewModel(
    val isLoading: Boolean = true,
    val todos: List<ToDoViewModel> = emptyList(),
    val error: ErrorMessage? = null,
    val addTodoViewModel: AddTodoViewModel? = null,
)

class AddTodoViewModel

data class ToDoViewModel(val id: String, val title: String, val starred: Boolean = false)

data class ErrorMessage(val message: String)