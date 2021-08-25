package com.pppp.todo.main.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.pppp.todo.main.MainActivity
import com.pppp.todo.main.view.com.TestViewModel
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.viewmodel.ToDoViewModel
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class ToDoListKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val viewModel = TestViewModel()

    @Test
    fun when_bar_then_fobar() {
        composeTestRule.setContent {
            MainContent()
        }
        viewModel._uiStates.tryEmit(MainViewState(todos = listOf(ToDoViewModel("", "", "Title"))))
    }
}