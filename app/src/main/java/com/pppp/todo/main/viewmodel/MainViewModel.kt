package com.pppp.todo.main.viewmodel

import com.pppp.entities.ToDo
import com.pppp.entities.User
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.main.viewmodel.MainViewEvent.*
import com.pppp.usecases.ToDosRepository.Companion.COMPLETED
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.todos.EditTodoUseCase.Params.Add
import com.pppp.usecases.todos.EditTodoUseCase.Params.Edit
import com.pppp.usecases.todos.GetToDoUseCase
import com.pppp.usecases.todos.GetToDoUseCase.Params.GetList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val editTodoUseCase: EditTodoUseCase,
    private val getToDoUseCase: GetToDoUseCase,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> MainViewState,
    private val user: User
) : GenericViewModelWithOneOffEvents<MainViewState, MainViewEvent, OneOffEvent>() {
    private lateinit var listId: String

    override val _uiStates = MutableStateFlow(MainViewState())

    override val _oneOffEvents = MutableSharedFlow<OneOffEvent>()

    override fun invoke(event: MainViewEvent) =
        when (event) {
            is OnToDoAdded -> addToDo(event.title, event.due)
            is OnToDoCompleted -> completeToDo(event.listId, event.itemId, event.completed)
            is OnAddToDoClicked -> TODO()
            is OnCancel -> TODO()
            is MainViewEvent.GetList -> getList(user.id, event.toDoList)
            is OnEditToDoClicked -> TODO()
        }

    private fun getList(userId: String, listId: String) {
        this.listId = listId
        launch {
            getToDoUseCase(GetList(userId, listId)).collect {
                emitViewState(mapper(it.filterNot { it.completed == true }))
            }
        }
    }

    private fun completeToDo(listId: String, itemId: String, completed: Boolean) =
        launch {
            editTodoUseCase(
                Edit(
                    userId = user.id,
                    listId = listId,
                    itemId = itemId,
                    values = mapOf(COMPLETED to completed)
                )
            )
        }

    private fun addToDo(title: String, due: Long?) =
        launch {
            editTodoUseCase(Add(userId = user.id, listId = listId, title = title, due = due))
        }
}