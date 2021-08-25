package com.pppp.todo.main.viewmodel

sealed class MainViewEvent {

    data class GetList(val toDoList: String?) : MainViewEvent()

    data class OnEditToDoClicked(val itemId: String, val listId: String) : MainViewEvent()

    object OnAddToDoClicked : MainViewEvent()

    data class OnToDoAdded(val title: String, val due: Long? = null) : MainViewEvent()

    data class OnToDoCompleted(val listId: String, val itemId: String, val completed: Boolean) : MainViewEvent()

    object OnCancel : MainViewEvent()
}