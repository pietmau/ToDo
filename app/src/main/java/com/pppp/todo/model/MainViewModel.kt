package com.pppp.todo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pppp.usecases.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun fff() {

    }

    init {
        viewModelScope.launch {
             repository.getToDos()
        }
    }
}