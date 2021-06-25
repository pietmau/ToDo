package com.pppp.usecases

interface UseCase<out Type, in Params> {

    suspend operator fun invoke(params: Params): Type
}