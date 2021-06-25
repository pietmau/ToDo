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

@HiltViewModel
class MainViewModel @Inject constructor(
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
                _uiState.value = mapper(it)
            }
        }
    }

    override fun invoke(event: ToDoViewEvent) {
        when (event) {
            is ToDoViewEvent.OnToDoAdded -> addTodoViewModel(event.text)
        }.exaustive
    }

    private fun addTodoViewModel(title: String) =
        viewModelScope.launch {
            addTodoViewModel.invoke(title)
        }
}