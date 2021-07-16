package com.pppp.todo.edittodo

import com.pppp.entities.ToDo
import com.pppp.entities.User
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.edittodo.EditTodoViewEvent.*
import com.pppp.todo.edittodo.OneOffEvents.OnCancel
import com.pppp.usecases.ToDosRepository.Companion.DUE
import com.pppp.usecases.ToDosRepository.Companion.TITLE
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.todos.GetToDoUseCase
import com.pppp.usecases.todos.GetToDoUseCase.Params.GetSingleToDo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
        private val getToDoUseCase: GetToDoUseCase,
        private val editTodoUseCase: EditTodoUseCase,
        private val user: User
) : GenericViewModelWithOneOffEvents<EditTodoViewState, EditTodoViewEvent, OneOffEvents>() {

    private var currentItem: ToDo? = null

    override val _uiStates = MutableStateFlow(EditTodoViewState())

    override val _oneOffEvents = MutableSharedFlow<OneOffEvents>()

    override fun invoke(event: EditTodoViewEvent) =
            when (event) {
                is Init -> onInit(event.listId, event.itemId)
                is OnBackPressed -> finish()
                is OnTextChanged -> emitViewState(state.copy(title = event.text))
                is OnDoneClicked -> onDoneClicked()
                is OnDateTimePicked -> emitViewState(state.copy(due = event.due))
            }

    private fun onInit(listId: String?, itemId: String?) {
        if (listId != null && itemId != null) {
            launch {
                getToDoUseCase.invoke(GetSingleToDo(
                        userId = user.id,
                        listId = listId,
                        itemId = itemId))
                        .collect {
                            this.currentItem = it.firstOrNull()
                            it.firstOrNull()?.let { emitViewState(it.toViewState()) }
                        }
            }
        } else {
            emitViewState(EditTodoViewState())
        }
    }

    private fun onDoneClicked() = launch {
        editTodoUseCase.invoke(
                EditTodoUseCase.Params.Edit(
                        userId = user.id,
                        listId = currentItem?.listId!!,
                        itemId = currentItem?.id!!,
                        values = state.toValueMap()
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
        val due: Long? = null,
        val listId: String = ""
)

sealed class EditTodoViewEvent {
    data class Init(val itemId: String?, val listId: String?) : EditTodoViewEvent()
    object OnBackPressed : EditTodoViewEvent()
    object OnDoneClicked : EditTodoViewEvent()
    data class OnTextChanged(val text: String) : EditTodoViewEvent()
    data class OnDateTimePicked(val due: Long) : EditTodoViewEvent()
}

sealed class OneOffEvents {
    object OnCancel : OneOffEvents()
}

private fun ToDo.toViewState(): EditTodoViewState =
        EditTodoViewState(
                isVisible = id != null,
                listId = listId,
                id = id!!,
                title = title,
                due = due
        )

private fun EditTodoViewState.toValueMap(): Map<String, Any?> = mapOf(TITLE to title, DUE to due)