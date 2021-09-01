package com.pppp.usecases.lists

import com.pppp.entities.ToDoList
import com.pppp.entities.User
import com.pppp.usecases.ListsRepository
import com.pppp.usecases.UseCase
import com.pppp.usecases.lists.EditListUseCase.Params
import com.pppp.usecases.lists.EditListUseCase.Params.Add

class EditListUseCase(private val repository: ListsRepository, private val user: User) :
    UseCase<Unit, Params> {

    override suspend fun invoke(params: Params) =
        when (params) {
            is Add -> addList(params)
            else -> throw UnsupportedOperationException()
        }

    private suspend fun addList(params: Add) {
        repository.addList(
            userId = user.id,
            toDoList = ToDoList(
                name = params.listTitle
            )
        )
    }

    sealed class Params {
        data class Edit(val toDoList: ToDoList) : Params()
        data class Add(val listTitle: String) : Params()
    }
}
