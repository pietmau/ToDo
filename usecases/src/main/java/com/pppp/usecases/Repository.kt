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

    companion object {
        const val ID = "id"
        const val TODOS = "todos"
        const val TITLE = "title"
        const val STARRED = "starred"
        const val CREATED = "created"
        const val COMPLETED = "completed"
        const val DUE = "due"
        const val EMPTY_STRING = ""
    }
}