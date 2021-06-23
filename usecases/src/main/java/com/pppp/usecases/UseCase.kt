package com.pppp.usecases

abstract class UseCase<out Type, in Params> {

    abstract suspend operator fun invoke(params: Params? = null): Type
}