package com.pppp.todo.main.view.com

import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.viewmodel.OneOffEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow


class TestViewModel :
    GenericViewModelWithOneOffEvents<MainViewState, MainViewEvent, OneOffEvent>() {

    public override val _uiStates: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState())
    public override val _oneOffEvents: MutableSharedFlow<OneOffEvent> = MutableSharedFlow()

    override fun invoke(t: MainViewEvent) {

    }
}