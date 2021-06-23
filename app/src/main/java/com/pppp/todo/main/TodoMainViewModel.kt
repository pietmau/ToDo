package com.pppp.todo.main

sealed class TodoMainViewModel {
    object Loading : TodoMainViewModel()
    data class Data(val todos: List<ToDoViewModel> = emptyList(), val error: ErrorMessage? = null) :
        TodoMainViewModel()
}

data class ToDoViewModel(val id: String, val title: String, val starred: Boolean = false)

data class ErrorMessage(val message: String)