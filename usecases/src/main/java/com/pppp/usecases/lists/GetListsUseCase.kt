package com.pppp.usecases.lists

import com.pppp.entities.ToDoList
import com.pppp.usecases.FlowUseCase
import com.pppp.usecases.ListsRepository
import kotlinx.coroutines.flow.Flow

class GetListsUseCase(private val listsRepository: ListsRepository) :
    FlowUseCase<List<ToDoList>, Nothing> {

    override suspend fun invoke(params: Nothing?): Flow<List<ToDoList>> = listsRepository.getLists()
}