package com.pppp.entities

data class ToDo(
    val id: String? = null,
    val title: String,
    val starred: Boolean = false,
    val created: Long? = null
)