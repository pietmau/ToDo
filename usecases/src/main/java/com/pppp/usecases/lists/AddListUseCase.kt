package com.pppp.usecases.lists

import com.pppp.entities.ToDoList
import com.pppp.usecases.UseCase
import com.pppp.usecases.lists.EditListUseCase.Params

class EditListUseCase : UseCase<Unit, Params> {

    override suspend fun invoke(params: Params) {
        TODO("Not yet implemented")
    }

    sealed class Params {
        data class Edit(val toDoList: ToDoList) : Params()
        data class Add(val toDoList: ToDoList) : Params()
    }
}
