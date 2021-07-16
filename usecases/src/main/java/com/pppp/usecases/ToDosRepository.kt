package com.pppp.usecases

import  com.pppp.entities.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDosRepository {

    suspend fun addToDo(params: Params.Add): String

    suspend fun edit(params: Params.Edit): String

    fun getToDo(id: String): Flow<List<ToDo>>

    fun getList(userId: String, listId: String): Flow<List<ToDo>>

    sealed class Params {
        data class Add(val userId: String, val listId: String, val title: String, val due: Long? = null)
        data class Edit(val userId: String, val listId: String, val itemId: String, val values: Map<String, Any?>)
    }

    companion object {
        const val ID = "id"
        const val TODOS = "todos"
        const val TITLE = "title"
        const val STARRED = "starred"
        const val CREATED = "created"
        const val COMPLETED = "completed"
        const val LIST_ID = "listId"
        const val DUE = "due"
        const val EMPTY_STRING = ""
        const val LISTS = "lists"
    }
}