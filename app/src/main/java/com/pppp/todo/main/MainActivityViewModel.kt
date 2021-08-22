package com.pppp.todo.main

import com.pppp.entities.User
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.main.MainActivityViewModel.Event
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListDismissed
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListClicked
import com.pppp.todo.main.MainActivityViewModel.OneOffEvent
import com.pppp.todo.main.MainActivityViewModel.ViewState
import com.pppp.usecases.main.LastVisitedListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val user: User,
    private val lastVisitedListRepository: LastVisitedListRepository
) :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    init {
        launch {
            val lastListId = lastVisitedListRepository.getLastVisitedList(user.id)
            _uiStates.emit(
                state.copy(
                    isLoading = false,
                    listId = lastListId
                )
            )
        }
    }

    override fun invoke(event: Event) =
        when (event) {
            OnNewListClicked -> emitViewState(state.copy(addNewListIsShowing = true))
            OnNewListDismissed -> emitViewState(state.copy(addNewListIsShowing = false))
            Event.OnDrawerOpened -> TODO()
        }

    data class ViewState(
        val isLoading: Boolean = true,
        val addNewListIsShowing: Boolean = false,
        val listId: String? = null
    )

    sealed class Event {
        object OnNewListClicked : Event()
        object OnNewListDismissed : Event()
        object OnDrawerOpened : Event()
    }

    sealed class OneOffEvent
}
