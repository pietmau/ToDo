package com.pppp.todo.main

sealed class ToDoViewEvent {
    object OnFabClicked : ToDoViewEvent()
}