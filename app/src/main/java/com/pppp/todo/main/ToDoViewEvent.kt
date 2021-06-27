package com.pppp.todo.main

sealed class ToDoViewEvent {

    data class OnEditToDoClicked(val id: String) : ToDoViewEvent()

    object OnAddToDoClicked : ToDoViewEvent()

    data class OnToDoAdded(val text: String, val due: Long? = null) : ToDoViewEvent()

    data class OnToDoCompleted(val id: String, val completed: Boolean) : ToDoViewEvent()
}