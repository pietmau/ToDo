package com.pppp.usecases.addtodo

import com.pppp.usecases.Repository
import com.pppp.usecases.UseCase

class AddToDoUseCase constructor(private val repository: Repository) : UseCase<Unit, String>() {

    override suspend fun invoke(params: String) {
        repository.addToDo(params)
    }
}