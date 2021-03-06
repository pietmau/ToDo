package com.pppp.todo.addtodo

import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.addtodo.Event.DoneClicked
import com.pppp.todo.addtodo.Event.Init
import com.pppp.todo.addtodo.Event.OnBackPressed
import com.pppp.todo.addtodo.Event.OnTimeDataPicked
import com.pppp.todo.addtodo.Event.OnTitleChanged
import com.pppp.todo.addtodo.OneOffEvent.AddToDo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class AddTodoViewModel @Inject constructor() :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    private lateinit var listId: String

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    override fun invoke(event: Event) =
        when (event) {
            is DoneClicked -> onDoneClicked()
            is OnTitleChanged -> onTitleChanged(event)
            is OnTimeDataPicked -> onTimeDataPicked(event.due)
            is OnBackPressed -> onBackPressed()
            is Init -> this.listId = event.listId
        }

    private fun onBackPressed() {
        clearState()
        emitOneOffEvent(OneOffEvent.OnBackPressed)
    }

    private fun onTimeDataPicked(due: Long?) =
        emitViewState(state.copy(due = due))

    private fun onTitleChanged(event: OnTitleChanged) =
        emitViewState(state.copy(title = event.title, isError = false))

    private fun onDoneClicked() =
        when {
            state.title.isBlank() -> emitViewState(state.copy(isError = true))
            else -> addTodo()
        }

    private fun addTodo() {
        emitOneOffEvent(AddToDo(title = state.title, due = state.due, listId = listId))
        clearState()
    }

    private fun clearState() = emitViewState(ViewState())
}

data class ViewState(
    val title: String = "",
    val isError: Boolean = false,
    val due: Long? = null
)

sealed class Event {
    object DoneClicked : Event()
    object OnBackPressed : Event()
    data class OnTitleChanged(val title: String) : Event()
    data class OnTimeDataPicked(val due: Long?) : Event()
    data class Init(val listId: String) : Event()
}

sealed class OneOffEvent {
    data class AddToDo(val listId: String, val title: String, val due: Long? = null) : OneOffEvent()
    object OnBackPressed : OneOffEvent()
}

