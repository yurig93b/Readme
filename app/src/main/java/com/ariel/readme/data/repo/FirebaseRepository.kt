package com.ariel.readme.data.repo


import com.ariel.readme.data.repo.ModeledDocumentChange.Companion.fromDocumentChanges
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IGetDocRef
import com.ariel.readme.data.repo.interfaces.IGetModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class FirebaseRepository<Model> {
    val collRef: CollectionReference
    protected abstract val rootNode: String

    protected fun HookGetDocumentSnapshot(
        t: Task<DocumentSnapshot>,
        models: IGetModel<Model>
    ): Task<DocumentSnapshot> {
        return t.addOnSuccessListener { value ->
            models.onSuccess(value.toObject(getModelClass())!!, value, value.reference)
        }.addOnFailureListener { e ->
            models.onFailure(e)
        }
    }

    protected inline fun <reified Model> HookQuery(
        t: Task<QuerySnapshot>,
        listener: IGetChangedModels<Model>
    ): Task<QuerySnapshot> {
        return t.addOnSuccessListener { value ->
            listener.onSuccess(fromDocumentChanges(value.documentChanges), value)
        }.addOnFailureListener { e ->
            listener.onFailure(e)
        }
    }

    protected inline fun <reified Model> HookListenQuery(
        q: Query,
        listener: IGetChangedModels<Model>
    ): ListenerRegistration {
        return q.addSnapshotListener { value, e ->
            if (e != null) {
                listener.onFailure(e)
                return@addSnapshotListener
            }
            listener.onSuccess(fromDocumentChanges(value!!.documentChanges), value)
        }
    }


    protected inline fun <reified Model> HookListenCollection(
        cRef: CollectionReference,
        listener: IGetChangedModels<Model>
    ): ListenerRegistration {
        return cRef.addSnapshotListener { value, e ->
            if (e != null) {
                listener.onFailure(e)
                return@addSnapshotListener
            }
            listener.onSuccess(fromDocumentChanges(value!!.documentChanges), value)
        }
    }


    protected inline fun <reified Model> HookDocumentListen(
        dRef: DocumentReference,
        listener: IGetChangedModel<Model>
    ): ListenerRegistration {
        return dRef.addSnapshotListener { value, e ->
            if (e != null) {
                listener.onFailure(e)
                return@addSnapshotListener
            }
            listener.onSuccess(value!!.toObject(Model::class.java), value)
        }
    }


    protected fun HookDocumentAddOrSet(
        t: Task<*>,
        listener: IGetDocRef
    ): Task<out Any> {
        return t.addOnSuccessListener { value ->
            listener.onSuccess(value as DocumentReference?)
        }.addOnFailureListener { e ->
            listener.onFailure(e)
        }
    }


    private fun getModelClass(): Class<Model> {
        val type: Type? = javaClass.genericSuperclass
        return (type as ParameterizedType).actualTypeArguments[0] as Class<Model>
    }

    init {
        collRef = Firebase.firestore.collection(rootNode)
    }
}