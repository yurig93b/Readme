package com.ariel.readme.understand

class OutClass<out T> {

    fun meow(): List<T>{
        return emptyList()
    }
}