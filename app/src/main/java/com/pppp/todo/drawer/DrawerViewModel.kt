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
import com.pppp.todo.drawer.ViewState.AddList.Hidden
import com.pppp.todo.drawer.ViewState.AddList.Showing

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val todoListsUseCase: GetListsUseCase,
    @UserId private val userId: String,
    private val mapper: @JvmSuppressWildcards (List<ToDoList>) -> ViewState
) : GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates = MutableStateFlow(ViewState())

    override val _oneOffEvents = MutableSharedFlow<OneOffEvent>()

    init {
        viewModelScope.launch {
            todoListsUseCase.invoke(GetListsUseCase.Params(userId)).collect {
                emitViewState(mapper(it))
            }
        }
    }

    override fun invoke(event: Event) {}

}

data class ViewState(
    val lists: List<ToDoList> = emptyList(),
    val addList: AddList = Hidden
) {

    data class ToDoList(
        val id: String? = null,
        val name: String,
        val created: Long? = null,
        val modified: Long? = null,
        val archived: Boolean = false,
        val deleted: Boolean = false,
        val priority: Long = 0
    )

    sealed class AddList {
        object Hidden : AddList()
        object Showing : AddList()
    }
}

sealed class Event

sealed class OneOffEvent