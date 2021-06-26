package com.pppp.todo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pppp.entities.ToDo
import com.pppp.todo.Consumer
import com.pppp.todo.exaustive
import com.pppp.usecases.addtodo.AddToDoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pppp.usecases.todolist.ToDoListUseCase
import kotlinx.coroutines.flow.collect
import com.pppp.todo.main.ToDoViewEvent.OnToDoAdded
import com.pppp.todo.main.ToDoViewEvent.OnToDoCompleted
import com.pppp.usecases.EditToDoUseCase
import com.pppp.usecases.EditToDoUseCase.Params

@HiltViewModel
class MainViewModel @Inject constructor(
    private val editToDoUseCase: EditToDoUseCase,
    private val toDoListUseCase: ToDoListUseCase,
    private val addTodoViewModel: AddToDoUseCase,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel
) : ViewModel(), Consumer<ToDoViewEvent> {

    private val _uiState: MutableStateFlow<TodoMainViewModel> =
        MutableStateFlow(TodoMainViewModel())

    val uiState: StateFlow<TodoMainViewModel> get() = _uiState

    init {
        viewModelScope.launch {
            toDoListUseCase().collect {
                _uiState.value = mapper(it.filterNot { it.completed == true })
            }
        }
    }

    override fun invoke(event: ToDoViewEvent) {
        when (event) {
            is OnToDoAdded -> addToDo(event.text)
            is OnToDoCompleted -> completeToDo(event.id, event.completed)
            is ToDoViewEvent.OnAddToDoClicked -> onAddToDoClicked()
        }.exaustive
    }

    private fun onAddToDoClicked() {
        val isAddTodoShowing = _uiState.value.isAddTodoShowing
        _uiState.value = _uiState.value.copy(isAddTodoShowing = !isAddTodoShowing)
    }

    private fun completeToDo(id: String, completed: Boolean) {
        viewModelScope.launch {
            editToDoUseCase.invoke(Params(id, mapOf("completed" to completed)))
        }
    }

    private fun addToDo(title: String) =
        viewModelScope.launch {
            addTodoViewModel.invoke(title)
        }
}