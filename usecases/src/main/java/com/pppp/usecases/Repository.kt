package com.pppp.usecases

import  com.pppp.entities.ToDo
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getToDo(): Flow<List<ToDo>>

    suspend fun addToDo(params: Params.Add): String

    suspend fun edit(id: String, values: Map<String, Any?>): String

    fun getToDo(id: String): Flow<List<ToDo>>

    sealed class Params {
        data class Add(val title: String, val due: Long? = null)
    }

}