package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.main.viewmodel.MainViewEvent.*
import com.pppp.todo.toDoViewModel
import com.pppp.uielements.fooLog
import com.pppp.usecases.EditTodoUseCase
import com.pppp.usecases.EditTodoUseCase.Params.Add
import com.pppp.usecases.EditTodoUseCase.Params.Edit
import com.pppp.usecases.todolist.GetToDoUseCase
import com.pppp.usecases.todolist.GetToDoUseCase.Params.GetSingle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val editTodoUseCase: EditTodoUseCase,
    private val getToDoUseCase: GetToDoUseCase,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> MainViewState
) : GenericViewModelWithOneOffEvents<MainViewState, MainViewEvent, OneOffEvent>() {

    override val _uiStates = MutableStateFlow(MainViewState())

    override val _oneOffEvents = MutableSharedFlow<OneOffEvent>()

    init {
        launch {
            getToDoUseCase().collect {
                emitViewState(mapper(it.filterNot { it.completed == true }))
            }
        }
    }

    override fun invoke(event: MainViewEvent) =
        when (event) {
            is OnToDoAdded -> addToDo(event.title, event.due)
            is OnToDoCompleted -> completeToDo(event.id, event.completed)
            is OnAddToDoClicked -> onAddToDoClicked()
            is OnEditToDoClicked -> onEditClicked(event.id)
            is OnCancel -> emitViewState(
                state.copy(
                    isAddTodoShowing = false,
                    toDoBeingEdited = null
                )
            )
        }

    private fun onEditClicked(id: String) {
        launch {
            getToDoUseCase(GetSingle(id)).collect {
                if (it.isNotEmpty()) {
                    emitViewState(state.copy(toDoBeingEdited = it.first().toDoViewModel()))
                }
            }
        }
    }

    private fun onAddToDoClicked() = launch {
        emitOneOffEvent(OneOffEvent.OpenAddToDoModal)
    }

    private fun completeToDo(id: String, completed: Boolean) =
        launch {
            editTodoUseCase(Edit(id, mapOf("completed" to completed)))
        }

    private fun addToDo(title: String, due: Long?) =
        launch {
            editTodoUseCase(Add(title = title, due = due))
        }

}