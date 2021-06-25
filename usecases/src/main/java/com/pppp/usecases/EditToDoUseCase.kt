package com.pppp.usecases

import com.pppp.usecases.EditToDoUseCase.Params

class EditToDoUseCase(private val repository: Repository) : UseCase<Unit, Params> {

    override suspend fun invoke(params:Params) {
        repository.edit(params.id, params.values)
    }

    data class Params(val id: String, val values: Map<String, Any?>)
}