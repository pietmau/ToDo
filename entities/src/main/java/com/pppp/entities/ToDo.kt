package com.pppp.entities

data class ToDo(
        val id: String? = null,
        val title: String,
        val starred: Boolean = false,
        val created: Long? = null,
        val completed: Boolean? = false,
        val due: Long? = null,
        val listId: String
)