package com.pppp.todo.main

sealed class ToDoViewEvent {
    data class OnToDoAdded(val text: String) : ToDoViewEvent()
}