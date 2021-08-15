package com.pppp.todo

import kotlinx.coroutines.flow.*

abstract class GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent> :
    GenericViewModel<ViewState, Event>() {

    protected abstract val _oneOffEvents: MutableSharedFlow<OneOffEvent>

    val oneOffEvents: Flow<OneOffEvent>
        get() = _oneOffEvents.onSubscription {  }

    protected fun emitOneOffEvent(event: OneOffEvent) {
        launch { _oneOffEvents.emit(event) }
    }
}