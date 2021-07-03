package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo
import com.pppp.todo.GenericViewModel
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoAdded
import com.pppp.todo.main.viewmodel.MainViewEvent.OnAddToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnEditToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoCompleted
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.todo.toDoViewModel
import com.pppp.usecases.EditTodoUseCase
import com.pppp.usecases.EditTodoUseCase.Params.Edit
import com.pppp.usecases.EditTodoUseCase.Params.Add
import com.pppp.usecases.todolist.GetToDoUseCase
import com.pppp.usecases.todolist.GetToDoUseCase.Params.GetSingle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val editTodoUseCase: EditTodoUseCase,
    private val getToDoUseCase: GetToDoUseCase,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> MainViewState
) : GenericViewModel<MainViewState, MainViewEvent, NavigationEvent>() {

    override val _uiState = MutableStateFlow(MainViewState())

    override val navigationEvent: Flow<NavigationEvent> = flowOf()

    init {
        launch {
            getToDoUseCase().collect {
                emit(mapper(it.filterNot { it.completed == true }))
            }
        }
    }

    override fun invoke(event: MainViewEvent) =
        when (event) {
            is OnToDoAdded -> addToDo(event.text, event.due)
            is OnToDoCompleted -> completeToDo(event.id, event.completed)
            is OnAddToDoClicked -> onAddToDoClicked()
            is OnEditToDoClicked -> onEditClicked(event.id)
            is OnCancel -> emit(state.copy(isAddTodoShowing = false, toDoBeingEdited = null))
        }

    private fun onEditClicked(id: String) {
        launch {
            getToDoUseCase(GetSingle(id)).collect {
                if (it.isNotEmpty()) {
                    emit(state.copy(toDoBeingEdited = it.first().toDoViewModel()))
                }
            }
        }
    }

    private fun onAddToDoClicked() = emit(state.copy(isAddTodoShowing = !state.isAddTodoShowing))

    private fun completeToDo(id: String, completed: Boolean) =
        launch {
            editTodoUseCase(Edit(id, mapOf("completed" to completed)))
        }

    private fun addToDo(title: String, due: Long?) =
        launch {
            editTodoUseCase(Add(title = title, due = due))
        }
}