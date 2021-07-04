package com.pppp.todo.edittodo

import com.pppp.entities.ToDo
import com.pppp.todo.GenericViewModel
import com.pppp.usecases.todolist.GetToDoUseCase
import com.pppp.usecases.todolist.GetToDoUseCase.Params.GetSingle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.pppp.todo.edittodo.EditTodoViewEvent.Init
import kotlinx.coroutines.flow.collect

@HiltViewModel
class EditViewModel @Inject constructor(val getToDoUseCase: GetToDoUseCase) :
    GenericViewModel<EditTodoViewState, EditTodoViewEvent>() {

    override val _uiStates = MutableStateFlow(EditTodoViewState())

    override fun invoke(event: EditTodoViewEvent) =
        when (event) {
            is Init -> onInit(event.toDoToBeEdited)
        }

    private fun onInit(toDoToBeEdited: String?) {
        if (toDoToBeEdited != null) {
            launch {
                getToDoUseCase.invoke(GetSingle(toDoToBeEdited))
                    .collect {
                        it.firstOrNull()?.let { emitViewState(it.toViewState()) }
                    }
            }
        } else {
            emitViewState(EditTodoViewState())
        }
    }
}

data class EditTodoViewState(
    val isVisible: Boolean = false,
    val id: String = "",
    val text: String = "",
    val due: Long? = null
)

sealed class EditTodoViewEvent {
    data class Init(val toDoToBeEdited: String?) : EditTodoViewEvent()
}

private fun ToDo.toViewState(): EditTodoViewState =
    EditTodoViewState(
        isVisible = id != null,
        id = id!!,
        text = title,
        due = due
    )