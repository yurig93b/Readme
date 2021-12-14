package com.ariel.readme.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.data.repo.ModeledDocumentChange
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot


class HotWordViewModel : ViewModel() {

    val hotWords : MutableLiveData<MutableList<String>> = MutableLiveData()
    //val uid : String = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        hotWords.value = mutableListOf()
        //HotWordRepository().listenOnHotWords(uid, object : IGetChangedModels<HotWord> {
        HotWordRepository().listenOnHotWords("1234", object : IGetChangedModels<HotWord> {

            override fun onSuccess(d: List<ModeledDocumentChange<HotWord>>, raw: QuerySnapshot?) {
                d!!.forEach {
                    word -> if(word.obj.word in hotWords.value!!){ hotWords.value!!.remove(word.obj.word)}
                            else{ hotWords.value!!.add(word.obj.word)}
                }
                hotWords.value = hotWords.value
                hotWords.value!!.sort()
            }

            override fun onFailure(e: Exception) {

            }

        })

    }
}