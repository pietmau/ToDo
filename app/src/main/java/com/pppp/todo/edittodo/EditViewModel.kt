package com.pppp.todo.edittodo

import com.pppp.todo.GenericViewModel
import com.pppp.todo.main.viewmodel.ToDoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor() :
    GenericViewModel<EditTodoViewState, EditTodoViewEvent>() {

    override val _uiState = MutableStateFlow(EditTodoViewState())

    override fun invoke(event: EditTodoViewEvent) =
        when (event) {
            is EditTodoViewEvent.Init -> event.toDoToBeEdited?.let { emit(it.toViewState()) }
        }
}

data class EditTodoViewState(
    val id: String = "",
    val text: String = "",
    val due: Long? = null
)

sealed class EditTodoViewEvent {
    data class Init(val toDoToBeEdited: ToDoViewModel?) : EditTodoViewEvent()
}

private fun ToDoViewModel.toViewState(): EditTodoViewState =
    EditTodoViewState(
        id = id,
        text = title,
        due = due
    )