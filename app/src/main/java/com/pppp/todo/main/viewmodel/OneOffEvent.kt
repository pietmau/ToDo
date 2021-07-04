package com.pppp.todo.main.viewmodel

sealed class OneOffEvent {
    object OpenAddToDoModal : OneOffEvent()
    object CloseAddToDoModal : OneOffEvent()
}