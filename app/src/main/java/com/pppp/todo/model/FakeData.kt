package com.pppp.todo.model

import com.pppp.entities.ToDo

object FakeData {

    fun toDos(): List<ToDo> = listOf(toDoItem("Foo", 0), toDoItem("Bar", 1))

    private fun toDoItem(title: String, id: Long) = ToDo(id, title)
}