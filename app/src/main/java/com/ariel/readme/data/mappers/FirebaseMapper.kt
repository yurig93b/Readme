package com.ariel.readme.data.mappers

import com.google.firebase.database.DataSnapshot
import java.lang.reflect.ParameterizedType


abstract class FirebaseMapper<Entity, Model>: IMapper<Entity, Model>{
    fun map(dataSnapshot: DataSnapshot): Model {
        val entity = dataSnapshot.getValue(getEntityClass())
        return map(entity!!)
    }

    open fun mapList(dataSnapshot: DataSnapshot): List<Model>? {
        val list: MutableList<Model> = ArrayList()
        for (item in dataSnapshot.children) {
            list.add(map(item))
        }
        return list
    }

    private fun getEntityClass(): Class<Entity> {
        val superclass: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        return superclass.getActualTypeArguments().get(0) as Class<Entity>
    }
}