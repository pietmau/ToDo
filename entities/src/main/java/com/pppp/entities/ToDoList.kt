package com.pppp.entities

data class ToDoList(
    val id: String? = null,
    val listId: String? = null,
    val created: Long? = null,
    val modified: Long? = null,
    val archived: Boolean = false,
    val deleted: Boolean = false,
    val priority: Long = 0
)