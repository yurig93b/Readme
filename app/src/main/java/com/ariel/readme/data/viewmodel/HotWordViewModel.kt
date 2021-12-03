package com.ariel.readme.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.data.repo.ModeledDocumentChange
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


class HotWordViewModel : ViewModel() {

    val hotWords : MutableLiveData<MutableList<HotWord>>

    init {
        hotWords = MutableLiveData()
        hotWords.value = mutableListOf()

        //val wordsDoc = HotWordRepository().getHotWords("1234")
        //val wordsDoc = HotWordRepository().getHotWords(FirebaseAuth.getInstance().currentUser!!.uid)
        //wordsDoc.addOnSuccessListener { doc -> doc.changes.forEach { word -> hotWords.value!!.add(word.obj) } }
        HotWordRepository().listenOnHotWords("1234", object : IGetChangedModels<HotWord> {

            override fun onSuccess(d: List<ModeledDocumentChange<HotWord>>, raw: QuerySnapshot?) {
                d!!.forEach { word -> hotWords.value!!.add(word.obj) }
                hotWords.value = hotWords.value
            }

            override fun onFailure(e: Exception) {
                TODO("Not yet implemented")
            }

        })

    }
}