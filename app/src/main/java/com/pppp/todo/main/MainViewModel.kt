package com.pppp.todo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pppp.entities.ToDo
import com.pppp.usecases.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val mapper: @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<TodoMainViewModel> =
        MutableStateFlow(TodoMainViewModel.Loading)

    val uiState: StateFlow<TodoMainViewModel> get() = _uiState

    init {
        viewModelScope.launch {
            try {
                val todos = repository.getToDos()
                _uiState.value = mapper(todos)
            } catch (exception: Exception) {
                _uiState.value =
                    TodoMainViewModel.Data(
                        error = ErrorMessage(
                            message = exception.message ?: ERROR
                        )
                    )
            }
        }
    }

    private companion object {
        private const val ERROR = "Error"
    }
}