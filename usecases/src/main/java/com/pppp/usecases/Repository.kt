package com.pppp.usecases

import  com.pppp.entities.ToDo
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getToDos(): Flow<List<ToDo>>

    suspend fun addToDo(params: String)

    fun edit(toDo1: String, toDo: Map<String, Any?>)
}