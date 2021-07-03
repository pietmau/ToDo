package com.pppp.todo

import kotlinx.coroutines.flow.*

abstract class GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent> :
    GenericViewModel<ViewState, Event>() {

    protected abstract val _oneOffEvents: MutableSharedFlow<OneOffEvent>

    val navigationEvents: Flow<OneOffEvent>
        get() = _oneOffEvents

    protected fun emitOneOffEvent(event: OneOffEvent) {
        launch { _oneOffEvents.emit(event) }
    }
}