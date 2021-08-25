package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo

data class MainViewState(
        val isLoading: Boolean = true,
        val todos: List<ToDoViewModel> = emptyList(),
        val error: ErrorMessage? = null
)

data class ToDoViewModel(
        val listId: String,
        val id: String,
        val title: String,
        val starred: Boolean = false,
        val due: Long? = null
)

data class ErrorMessage(val message: String)