package com.pppp.usecases

import com.pppp.entities.ToDo
import com.pppp.entities.ToDoList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    suspend fun getLists(): Flow<List<ToDoList>>

    suspend fun addList(toDoList: ToDoList): String

    suspend fun editList(toDoList: ToDoList): String

    companion object {
        const val ID = "id"
        const val USERS = "users"
        const val LISTS = "lists"
        const val LIST_ID = "list_id"
        const val PRIORITY = "priority"
        const val DELETED = "deleted"
        const val ARCHIVED = "archived"
        const val CREATED = "created"
        const val MODIFIED = "created"
    }
}