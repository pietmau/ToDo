package com.pppp.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class GenericViewModel<ViewState, Event, NavigationEvent> : ViewModel(), Consumer<Event> {

    protected val state: ViewState
        get() = _uiState.value

    protected abstract val _uiState: MutableStateFlow<ViewState>

    protected val uiState: StateFlow<ViewState> get() = _uiState

    protected abstract val navigationEvent: Flow<NavigationEvent>

    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    protected fun emit(value: ViewState) {
        _uiState.value = value
    }
}