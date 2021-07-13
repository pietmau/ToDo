package com.pppp.usecases.lists

import com.pppp.entities.ToDoList
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.ListsRepository
import kotlinx.coroutines.flow.Flow

class GetListsUseCase(private val listsRepository: ListsRepository, ) :
    FlowUseCase<List<ToDoList>, GetListsUseCase.Params> {

    override suspend fun invoke(params: Params): Flow<List<ToDoList>> = listsRepository.getLists(params.userId)

    data class Params(val userId: String)
}