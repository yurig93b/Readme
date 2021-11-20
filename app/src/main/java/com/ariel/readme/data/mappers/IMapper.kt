package com.ariel.readme.data.mappers

interface IMapper<From, To> {
    fun map(from: From): To;
}