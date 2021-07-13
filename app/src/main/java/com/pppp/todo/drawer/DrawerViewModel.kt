package com.pppp.todo.drawer

import androidx.lifecycle.viewModelScope
import com.pppp.entities.ToDoList
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.UserId
import com.pppp.usecases.lists.GetListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val todoListsUseCase: GetListsUseCase,
    @UserId private val userId: String,
    private val mapper: @JvmSuppressWildcards (List<ToDoList>) -> ViewState
) :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    init {
        viewModelScope.launch {
            todoListsUseCase.invoke(GetListsUseCase.Params(userId)).collect {
                emitViewState(mapper(it))
            }
        }
    }

    private fun foo() {

    }

    override val _uiStates = MutableStateFlow(ViewState())

    override val _oneOffEvents = MutableSharedFlow<OneOffEvent>()

    override fun invoke(event: Event) {

    }
}

data class ViewState(val lists: List<ToDoList> = emptyList()) {

    data class ToDoList(
        val id: String? = null,
        val name: String,
        val listId: String? = null,
        val created: Long? = null,
        val modified: Long? = null,
        val archived: Boolean = false,
        val deleted: Boolean = false,
        val priority: Long = 0
    )
}

sealed class Event
sealed class OneOffEvent