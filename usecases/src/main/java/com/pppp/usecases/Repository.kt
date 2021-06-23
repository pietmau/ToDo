package com.pppp.usecases

import com.sun.tools.javac.comp.Todo

interface Repository {

    suspend fun getToDos(): List<Todo>
}