package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.main.viewmodel.MainViewEvent.OnAddToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.todo.main.viewmodel.MainViewEvent.OnEditToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoAdded
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoCompleted
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.todos.EditTodoUseCase.Params.Add
import com.pppp.usecases.todos.EditTodoUseCase.Params.Edit
import com.pppp.usecases.ToDosRepository.Companion.COMPLETED
import com.pppp.usecases.todos.GetToDoUseCase
import com.pppp.usecases.todos.GetToDoUseCase.Params.GetAll
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
            getToDoUseCase(GetAll).collect {
                emitViewState(mapper(it.filterNot { it.completed == true }))
            }
        }
    }

    override fun invoke(event: MainViewEvent) = when (event) {
        is OnToDoAdded -> addToDo(event.title, event.due)
        is OnToDoCompleted -> completeToDo(event.id, event.completed)
        is OnAddToDoClicked -> onAddToDoClicked()
        is OnEditToDoClicked -> onEditClicked(event.id)
        is OnCancel -> onCancel()
    }

    private fun onCancel() =
        emitViewState(state.copy(itemBeingEdited = null, addToDo = AddToDo.Hidden))

    private fun onEditClicked(id: String) = emitViewState(state.copy(itemBeingEdited = id))

    private fun onAddToDoClicked() = emitViewState(state.copy(addToDo = AddToDo.Showing))

    private fun completeToDo(id: String, completed: Boolean) = launch {
        editTodoUseCase(Edit(id, mapOf(COMPLETED to completed)))
    }

    private fun addToDo(title: String, due: Long?) = launch {
        editTodoUseCase(Add(title = title, due = due))

    }
}