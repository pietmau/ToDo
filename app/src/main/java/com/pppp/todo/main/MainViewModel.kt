package com.pppp.todo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pppp.entities.ToDo
import com.pppp.todo.Consumer
import com.pppp.todo.exaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pppp.usecases.todolist.ToDoListUseCase

@HiltViewModel
class MainViewModel @Inject constructor(
    private val toDoListUseCase: ToDoListUseCase,
    private val mapper: @JvmSuppressWildcards (Result<List<ToDo>>) -> TodoMainViewModel
) : ViewModel(), Consumer<ToDoViewEvent> {

    private val _uiState: MutableStateFlow<TodoMainViewModel> =
        MutableStateFlow(TodoMainViewModel())
    val uiState: StateFlow<TodoMainViewModel> get() = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = mapper(toDoListUseCase())
        }
    }

    override fun invoke(event: ToDoViewEvent) {
        when (event) {
            is ToDoViewEvent.OnFabClicked -> {
                _uiState.value = _uiState.value.copy(addTodoViewModel = AddTodoViewModel())
            }
        }.exaustive
    }
}