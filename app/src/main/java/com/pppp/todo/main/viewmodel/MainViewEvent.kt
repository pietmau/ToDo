package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDoList

sealed class MainViewEvent {

    data class GetList(val toDoList: String) : MainViewEvent()

    data class OnEditToDoClicked(val id: String) : MainViewEvent()

    object OnAddToDoClicked : MainViewEvent()

    data class OnToDoAdded(val title: String, val due: Long? = null) : MainViewEvent()

    data class OnToDoCompleted(val listId: String, val itemId: String, val completed: Boolean) : MainViewEvent()

    object OnCancel : MainViewEvent()
}