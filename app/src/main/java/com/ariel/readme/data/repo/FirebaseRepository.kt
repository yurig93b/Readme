package com.ariel.readme.data.repo


import com.ariel.readme.data.repo.ModeledDocumentChange.Companion.fromDocumentChanges
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class FirebaseRepository<Model> {
    protected val collRef: CollectionReference
    protected abstract val rootNode: String

    protected fun HookGetDocumentSnapshot(
        t: Task<DocumentSnapshot>
    ): Task<ModeledDocument<Model>> {
        return t.continueWith(object : Continuation<DocumentSnapshot, ModeledDocument<Model>> {
            override fun then(tres: Task<DocumentSnapshot>): ModeledDocument<Model> {
                if (tres.exception != null) {
                    throw tres.exception!!
                }

                val res = if (tres.getResult() != null) tres.getResult()!!
                    .toObject(getModelClass()) else null
                return ModeledDocument(res, t.getResult())
            }
        })
    }

    protected inline fun <reified Model> HookQuery(
        t: Task<QuerySnapshot>,
    ): Task<ModeledChangedDocuments<Model>> {

        return t.continueWith(object : Continuation<QuerySnapshot, ModeledChangedDocuments<Model>> {
            override fun then(tres: Task<QuerySnapshot>): ModeledChangedDocuments<Model> {
                if (tres.exception != null) {
                    throw tres.exception!!
                }

                val docChanges = if (tres.result != null) tres.result!!.documentChanges else null
                return ModeledChangedDocuments(fromDocumentChanges(docChanges), tres.getResult())

            }
        })
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


    protected inline fun <reified Model> HookListenDocument(
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


    private fun getModelClass(): Class<Model> {
        val type: Type? = javaClass.genericSuperclass
        return (type as ParameterizedType).actualTypeArguments[0] as Class<Model>
    }

    init {
        Firebase.firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
        collRef = Firebase.firestore.collection(rootNode)
    }
}