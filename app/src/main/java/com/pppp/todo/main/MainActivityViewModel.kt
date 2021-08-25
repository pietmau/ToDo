package com.pppp.todo.main

import com.pppp.entities.User
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.main.MainActivityViewModel.Event
import com.pppp.todo.main.MainActivityViewModel.Event.OnEditClicked
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewItemClicked
import com.pppp.todo.main.MainActivityViewModel.Event.OnDrawerOpened
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListDismissed
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListClicked
import com.pppp.todo.main.MainActivityViewModel.Event.OnEditDismissed
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewItemDismissed
import com.pppp.todo.main.MainActivityViewModel.Event.OnToDoAdded
import com.pppp.todo.main.MainActivityViewModel.OneOffEvent
import com.pppp.todo.main.MainActivityViewModel.ViewState
import com.pppp.todo.main.MainActivityViewModel.ViewState.EditItem.Some
import com.pppp.usecases.main.LastVisitedListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import com.pppp.todo.main.MainActivityViewModel.ViewState.EditItem.None
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.todos.EditTodoUseCase.Params.Add

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val user: User,
    private val lastVisitedListRepository: LastVisitedListRepository, // TODO create usecase instead
    private val editTodoUseCase: EditTodoUseCase
) : GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    init {
        launch {
            val lastListId = lastVisitedListRepository.getLastVisitedList(user.id)
            _uiStates.emit(
                state.copy(
                    isLoading = false,
                    listId = lastListId
                )
            )
        }
    }

    override fun invoke(event: Event) =
        when (event) {
            is OnNewListClicked -> onNewListClicked()
            is OnNewListDismissed -> resetDialogs()
            is OnDrawerOpened -> resetDialogs()
            is OnEditClicked -> onEditClicked(event)
            is OnNewItemClicked -> onNewItemClicked()
            is OnEditDismissed -> resetDialogs()
            is OnNewItemDismissed -> resetDialogs()
            is OnToDoAdded -> onToDoAdded(event)
        }

    private fun onToDoAdded(event: OnToDoAdded) {
        resetDialogs()
        launch {
            editTodoUseCase.invoke(
                Add(
                    listId = event.listId,
                    title = event.title,
                    due = event.due
                )
            )
        }
    }

    private fun onNewItemClicked() {
        resetDialogs()
        emitViewState(state.copy(addNewItemIsShowing = true))
    }

    private fun onNewListClicked() {
        resetDialogs()
        emitViewState(state.copy(addNewListIsShowing = true))
    }

    private fun onEditClicked(event: OnEditClicked) {
        resetDialogs()
        emitViewState(
            state.copy(
                addNewListIsShowing = false,
                itemBeingEdited = Some(
                    listId = event.listId,
                    itemId = event.itemId
                )
            )
        )
    }

    private fun resetDialogs() {
        emitViewState(
            state.copy(
                addNewListIsShowing = false,
                addNewItemIsShowing = false,
                itemBeingEdited = None
            )
        )
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val addNewListIsShowing: Boolean = false,
        val listId: String? = null,
        val addNewItemIsShowing: Boolean = false,
        val itemBeingEdited: EditItem = None
    ) {
        sealed class EditItem {
            object None : EditItem()
            data class Some(val listId: String, val itemId: String) : EditItem()
        }
    }

    sealed class Event {
        object OnNewListClicked : Event()
        object OnNewListDismissed : Event()
        object OnDrawerOpened : Event()
        data class OnEditClicked(val listId: String, val itemId: String) : Event()
        object OnNewItemClicked : Event()
        data class OnToDoAdded(
            val listId: String,
            val title: String,
            val due: Long? = null
        ) : Event()

        object OnNewItemDismissed : Event()
        object OnEditDismissed : Event()
    }

    sealed class OneOffEvent
}
