package com.pppp.usecases.addtodo

import com.pppp.usecases.Repository
import com.pppp.usecases.UseCase
import com.pppp.usecases.addtodo.AddToDoUseCase.Params

class AddToDoUseCase(private val repository: Repository) : UseCase<String, Params> {

    override suspend fun invoke(params: Params) =
        repository.addToDo(params)

    data class Params(val title: String, val due: Long? = null)
}