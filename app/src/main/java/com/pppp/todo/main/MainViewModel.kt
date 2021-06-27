package com.pppp.todo.main

import com.pppp.entities.ToDo
import com.pppp.todo.GenericViewModel
import com.pppp.todo.main.ToDoViewEvent.OnToDoAdded
import com.pppp.todo.main.ToDoViewEvent.OnAddToDoClicked
import com.pppp.todo.main.ToDoViewEvent.OnEditToDoClicked
import com.pppp.todo.main.ToDoViewEvent.OnToDoCompleted
import com.pppp.todo.toDoViewModel
import com.pppp.usecases.EditTodoUseCase
import com.pppp.usecases.EditTodoUseCase.Params.Edit
import com.pppp.usecases.EditTodoUseCase.Params.Add
import com.pppp.usecases.todolist.GetToDoUseCase
import com.pppp.usecases.todolist.GetToDoUseCase.Params.GetSingle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val editTodoUseCase: EditTodoUseCase,
    private val getToDoUseCase: GetToDoUseCase,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel
) : GenericViewModel<TodoMainViewModel, ToDoViewEvent>() {

    override val _uiState = MutableStateFlow(TodoMainViewModel())

    init {
        launch {
            getToDoUseCase().collect {
                emit(mapper(it.filterNot { it.completed == true }))
            }
        }
    }

    override fun invoke(event: ToDoViewEvent) =
        when (event) {
            is OnToDoAdded -> addToDo(event.text, event.due)
            is OnToDoCompleted -> completeToDo(event.id, event.completed)
            is OnAddToDoClicked -> onAddToDoClicked()
            is OnEditToDoClicked -> onEditClicked(event.id)
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