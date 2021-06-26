package com.pppp.usecases

import  com.pppp.entities.ToDo
import com.pppp.usecases.addtodo.AddToDoUseCase
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getToDos(): Flow<List<ToDo>>

    suspend fun addToDo(params: AddToDoUseCase.Params)

    fun edit(id: String, values: Map<String, Any?>)
}