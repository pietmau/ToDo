package com.pppp.todo.main

sealed class ToDoViewEvent {
    data class OnToDoAdded(val text: String) : ToDoViewEvent()
    data class OnToDoCompleted(val id: String, val completed: Boolean) : ToDoViewEvent()
}