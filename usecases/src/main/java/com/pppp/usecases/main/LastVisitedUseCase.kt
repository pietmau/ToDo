package com.pppp.usecases.main

import com.pppp.usecases.UseCase
import com.pppp.usecases.main.LastVisitedUseCase.ListReference
import com.pppp.usecases.main.LastVisitedUseCase.Params

class LastVisitedUseCase(private val repository: LastVisitedListRepository) :
    UseCase<ListReference, Params> {

    override suspend fun invoke(params: Params) =
        repository.getLastVisitedList("")?.let { ListReference.Some(it) }
            ?: ListReference.None

    data class Params(val userId: String)

    sealed class ListReference {
        object None : ListReference()
        data class Some(val listId: String) : ListReference()
    }
}
