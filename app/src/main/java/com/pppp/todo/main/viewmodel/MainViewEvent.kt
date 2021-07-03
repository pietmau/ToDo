package com.pppp.todo.main.viewmodel

sealed class MainViewEvent {

    data class OnEditToDoClicked(val id: String) : MainViewEvent()

    object OnAddToDoClicked : MainViewEvent()

    data class OnToDoAdded(val text: String, val due: Long? = null) : MainViewEvent()

    data class OnToDoCompleted(val id: String, val completed: Boolean) : MainViewEvent()

    object OnCancel : MainViewEvent()
}