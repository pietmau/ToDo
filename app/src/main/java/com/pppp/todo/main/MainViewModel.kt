package com.pppp.todo.main

import com.pppp.entities.ToDo
import com.pppp.todo.GenericViewModel
import com.pppp.todo.main.ToDoViewEvent.OnToDoAdded
import com.pppp.todo.main.ToDoViewEvent.OnToDoCompleted
import com.pppp.usecases.EditToDoUseCase
import com.pppp.usecases.addtodo.AddToDoUseCase
import com.pppp.usecases.notification.NotificationUseCase
import com.pppp.usecases.todolist.ToDoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val editToDoUseCase: EditToDoUseCase,
    private val toDoListUseCase: ToDoListUseCase,
    private val addTodoViewModel: AddToDoUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel
) : GenericViewModel<TodoMainViewModel, ToDoViewEvent>() {

    override val _uiState: MutableStateFlow<TodoMainViewModel> =
        MutableStateFlow(TodoMainViewModel())

    init {
        launch {
            toDoListUseCase().collect {
                emit(mapper(it.filterNot { it.completed == true }))
            }
        }
    }

    override fun invoke(event: ToDoViewEvent) =
        when (event) {
            is OnToDoAdded -> addToDo(event.text, event.due)
            is OnToDoCompleted -> completeToDo(event.id, event.completed)
            is ToDoViewEvent.OnAddToDoClicked -> onAddToDoClicked()
        }

    private fun onAddToDoClicked() {
        emit(state.copy(isAddTodoShowing = !state.isAddTodoShowing))
    }

    private fun completeToDo(id: String, completed: Boolean) {
        launch {
            editToDoUseCase(EditToDoUseCase.Params(id, mapOf("completed" to completed)))
        }
    }

    private fun addToDo(title: String, due: Long?) =
        launch {
            val id = addTodoViewModel(
                AddToDoUseCase.Params(
                    title = title,
                    due = due
                )
            )
            notificationUseCase(
                NotificationUseCase.Params(
                    text = title,
                    timeInMills = due,
                    id = id,
                )
            )
        }
}