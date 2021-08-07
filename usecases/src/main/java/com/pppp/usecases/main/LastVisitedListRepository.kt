package com.pppp.usecases.main

import kotlinx.coroutines.flow.Flow

interface LastVisitedListRepository {
    suspend fun getLastVisitedList(userId: String): String?
    suspend fun addList(userId: String, listId: String): String
}