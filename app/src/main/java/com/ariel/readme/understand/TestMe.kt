package com.ariel.readme.understand

class TestMe {

    fun consume(c: InClass<B>){
        c.meow(C())
    }

    init {

        val a = A()
        val b = B()
        val c = C()

        val s: InClass<A> = InClass()

        consume(s)
    }
}