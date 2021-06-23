package com.pppp.usecases

import  com.pppp.entities.ToDo

interface Repository {

    suspend fun getToDos(): List<ToDo>
}