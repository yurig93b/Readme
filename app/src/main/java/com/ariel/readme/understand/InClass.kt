package com.ariel.readme.understand

class InClass<in T> {
    private val contents = mutableListOf<T>()

    fun meow(v: T){
        contents.add(v)
    }
}