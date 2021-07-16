package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo

data class MainViewState(
        val isLoading: Boolean = true,
        val todos: List<ToDoViewModel> = emptyList(),
        val error: ErrorMessage? = null,
        val itemBeingEdited: ItemBeingEdited = ItemBeingEdited.None,
        val addToDo: AddToDo = AddToDo.Hidden
)

sealed class ItemBeingEdited {
    object None : ItemBeingEdited()
    data class Some(val itemId: String, val listId: String) : ItemBeingEdited()
}

sealed class AddToDo {
    object Hidden : AddToDo()
    object Showing : AddToDo()
}

data class ToDoViewModel(
        val listId: String,
        val id: String,
        val title: String,
        val starred: Boolean = false,
        val due: Long? = null
)

data class ErrorMessage(val message: String)