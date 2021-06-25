package com.pppp.usecases

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<out Type, in Params> {

    abstract suspend operator fun invoke(params: Params? = null): Flow<Type>

}