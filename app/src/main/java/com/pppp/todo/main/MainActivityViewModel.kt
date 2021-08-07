package com.pppp.todo.main

import com.pppp.entities.User
import com.pppp.todo.GenericViewModelWithOneOffEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import com.pppp.todo.main.MainActivityViewModel.OneOffEvent
import com.pppp.todo.main.MainActivityViewModel.ViewState
import com.pppp.todo.main.MainActivityViewModel.Event
import com.pppp.usecases.main.LastVisitedListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.pppp.todo.main.MainActivityViewModel.Event.Init
import com.pppp.todo.main.MainActivityViewModel.ViewState.None
import com.pppp.todo.main.MainActivityViewModel.ViewState.Loading
import com.pppp.todo.main.MainActivityViewModel.ViewState.Content
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val user: User,
    private val lastVisitedListRepository: LastVisitedListRepository
) :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(Loading)

    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    override val states: StateFlow<ViewState>
        get() = _uiStates

    override fun invoke(event: Event) =
        when (event) {
            is Init -> start()
        }

    private fun start() {
        launch {
            val lastListId = lastVisitedListRepository.getLastVisitedList(user.id)
            val viewState = lastListId?.let { Content(it) } ?: None
            _uiStates.emit(viewState)
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        object None : ViewState()
        data class Content(val listId: String) : ViewState()
    }

    sealed class Event {
        object Init : Event()
    }

    sealed class OneOffEvent
}
