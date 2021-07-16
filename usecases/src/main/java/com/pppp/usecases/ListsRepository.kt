package com.pppp.usecases

import com.pppp.entities.ToDoList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    suspend fun getLists(userId: String): Flow<List<ToDoList>>

    suspend fun addList(userId: String, toDoList: ToDoList): String

    suspend fun editList(toDoList: ToDoList): String

    companion object {
        const val NAME = "name"
        const val ID = "id"
        const val USERS = "users"
        const val LISTS = "lists"
        const val LIST_ID = "listId"
        const val PRIORITY = "priority"
        const val DELETED = "deleted"
        const val ARCHIVED = "archived"
        const val CREATED = "created"
        const val MODIFIED = "created"
    }
}