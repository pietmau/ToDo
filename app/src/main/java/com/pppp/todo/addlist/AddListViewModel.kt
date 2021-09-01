package com.pppp.todo.addlist

import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.addlist.AddListViewModel.Event
import com.pppp.todo.addlist.AddListViewModel.Event.OnSaveClicked
import com.pppp.todo.addlist.AddListViewModel.Event.OnTextChanged
import com.pppp.todo.addlist.AddListViewModel.Event.OnVisibilityChanged
import com.pppp.todo.addlist.AddListViewModel.OneOffEvent
import com.pppp.todo.addlist.AddListViewModel.ViewState
import com.pppp.usecases.lists.EditListUseCase
import com.pppp.usecases.lists.EditListUseCase.Params.Add
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class AddListViewModel @Inject constructor(private val editListUseCase: EditListUseCase) :
    GenericViewModelWithOneOffEvents<ViewState, Event, OneOffEvent>() {

    override val _uiStates: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    override fun invoke(t: Event) =
        when (t) {
            is OnVisibilityChanged -> emitViewState(ViewState(isVisible = t.isVisible))
            is OnTextChanged -> emitViewState(state.copy(text = t.text))
            is OnSaveClicked -> onSaveClicked()
        }

    private fun onSaveClicked() {
        if (state.text.isNotBlank()) {
            val listTitle = state.text
            emitOneOffEvent(OneOffEvent.OnSaveClicked)
            emitViewState(ViewState())
            launch {
                editListUseCase.invoke(Add(listTitle))
            }

        } else {
            onError()
        }
    }

    private fun onError() {
        emitViewState(state.copy(isError = true))
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val text: String = EMPTY_STRING,
        val isError: Boolean = false
    )

    sealed class Event {
        data class OnVisibilityChanged(val isVisible: Boolean) : Event()
        data class OnTextChanged(val text: String) : Event()
        object OnSaveClicked : Event()
    }

    sealed class OneOffEvent {
        object OnSaveClicked : OneOffEvent()
    }
}