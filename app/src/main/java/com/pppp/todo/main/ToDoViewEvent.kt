package com.pppp.todo.main

sealed class ToDoViewEvent {
    object OnAddToDoClicked : ToDoViewEvent()
    data class OnToDoAdded(val text: String, val due: Long? = null) : ToDoViewEvent()
    data class OnToDoCompleted(val id: String, val completed: Boolean) : ToDoViewEvent()
}