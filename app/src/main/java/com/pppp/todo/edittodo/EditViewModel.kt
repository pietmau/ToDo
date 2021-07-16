package com.pppp.todo.edittodo

import com.pppp.entities.ToDo
import com.pppp.entities.User
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.edittodo.EditTodoViewEvent.Init
import com.pppp.todo.edittodo.EditTodoViewEvent.OnBackPressed
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDateTimePicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDoneClicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnTextChanged
import com.pppp.todo.edittodo.OneOffEvents.OnCancel
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.ToDosRepository.Companion.DUE
import com.pppp.usecases.ToDosRepository.Companion.TITLE
import com.pppp.usecases.todos.GetToDoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
        private val getToDoUseCase: GetToDoUseCase,
        private val editTodoUseCase: EditTodoUseCase,
        private val user: User
) : GenericViewModelWithOneOffEvents<EditTodoViewState, EditTodoViewEvent, OneOffEvents>() {
    private lateinit var currentItem: ToDo

    override val _uiStates = MutableStateFlow(EditTodoViewState())

    override val _oneOffEvents = MutableSharedFlow<OneOffEvents>()

    override fun invoke(event: EditTodoViewEvent) =
            when (event) {
                is Init -> emitViewState(event.toDo.toViewState())
                is OnBackPressed -> finish()
                is OnTextChanged -> emitViewState(state.copy(title = event.text))
                is OnDoneClicked -> onDoneClicked()
                is OnDateTimePicked -> emitViewState(state.copy(due = event.due))
            }

    private fun onDoneClicked() = launch {
        editTodoUseCase.invoke(
                EditTodoUseCase.Params.Edit(
                        itemId = state.id,
                        values = state.toValueMap(),
                        userId = user.id,
                        listId = currentItem.listId
                )
        )
        emitViewState(EditTodoViewState())
    }

    private fun finish() {
        emitOneOffEvent(OnCancel)
        emitViewState(EditTodoViewState())
    }
}

data class EditTodoViewState(
        val isVisible: Boolean = false,
        val id: String = "",
        val title: String = "",
        val due: Long? = null
)

sealed class EditTodoViewEvent {
    data class Init(val toDo: ToDo?) : EditTodoViewEvent()
    object OnBackPressed : EditTodoViewEvent()
    object OnDoneClicked : EditTodoViewEvent()
    data class OnTextChanged(val text: String) : EditTodoViewEvent()
    data class OnDateTimePicked(val due: Long) : EditTodoViewEvent()
}

sealed class OneOffEvents {
    object OnCancel : OneOffEvents()
}

private fun ToDo?.toViewState(): EditTodoViewState =
        this?.let {
            EditTodoViewState(
                    isVisible = id != null,
                    id = id!!,
                    title = title,
                    due = due
            )
        } ?: EditTodoViewState()

private fun EditTodoViewState.toValueMap(): Map<String, Any?> = mapOf(TITLE to title, DUE to due)