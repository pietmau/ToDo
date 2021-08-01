package com.pppp.todo.main

import com.pppp.todo.GenericViewModelWithOneOffEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import com.pppp.todo.main.MainActivityViewModel.OneOffEvent
import com.pppp.todo.main.MainActivityViewModel.ViewState
import com.pppp.todo.main.MainActivityViewModel.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.None)
    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    override fun invoke(t: Event) {

    }

    sealed class ViewState {
        object None : ViewState()
        data class Content(val listId: String) : ViewState()
    }

    sealed class Event
    sealed class OneOffEvent
}
