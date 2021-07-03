package com.pppp.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class GenericViewModel<ViewState, Event> : ViewModel(), Consumer<Event> {

    protected val state: ViewState
        get() = _uiStates.value

    protected abstract val _uiStates: MutableStateFlow<ViewState>

    val states: StateFlow<ViewState>
        get() = _uiStates
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    protected fun emitViewState(state: ViewState) {
        _uiStates.value = state
    }
}