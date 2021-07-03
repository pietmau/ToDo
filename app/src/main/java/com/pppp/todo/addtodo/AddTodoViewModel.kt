package com.pppp.todo.addtodo

import com.pppp.todo.GenericViewModelWithOneOffEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import com.pppp.todo.addtodo.Event.DoneClicked
import com.pppp.todo.addtodo.Event.OnTitleChanged
import com.pppp.todo.addtodo.OneOffEvent.AddToDo

@HiltViewModel
class AddTodoViewModel @Inject constructor() :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    override fun invoke(event: Event) =
        when (event) {
            is DoneClicked -> onDoneClicked()
            is OnTitleChanged -> onTitleChanged(event)
        }

    private fun onTitleChanged(event: OnTitleChanged) =
        emitViewState(state.copy(title = event.title, isError = false))

    private fun onDoneClicked() =
        when {
            state.title.isBlank() -> emitViewState(state.copy(isError = true))
            else -> emitOneOffEvent(AddToDo(title = state.title, due = state.due))
        }

}

data class ViewState(
    val title: String = "",
    val isError: Boolean = false,
    val due: Long? = null
)

sealed class Event {
    object DoneClicked : Event()
    data class OnTitleChanged(val title: String) : Event()
}

sealed class OneOffEvent {
    data class AddToDo(val title: String, val due: Long? = null) : OneOffEvent()
}

