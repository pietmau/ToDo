package com.pppp.usecases

import kotlinx.coroutines.flow.Flow

interface FlowUseCase<out Type, in Params> {

    suspend operator fun invoke(params: Params? = null): Flow<Type>

}