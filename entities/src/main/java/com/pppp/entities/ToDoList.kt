package com.pppp.entities

data class ToDoList(
    val items: List<ToDo>,
    val created: Long,
    val modified: Long,
    val archived: Boolean = false,
    val deleted: Boolean = false
)