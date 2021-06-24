package com.pppp.todo

val Any?.exaustive
    get() = Unit

interface Consumer<T> {
    operator fun invoke(t: T)
}