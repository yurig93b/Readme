package com.ariel.readme.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.repo.HotWordRepository
import com.google.firebase.auth.FirebaseAuth


class HotWordViewModel : ViewModel() {

    val hotWords : MutableLiveData<MutableList<HotWord>> = MutableLiveData()

    init {
        val wordsDoc = HotWordRepository().getHotWords("1234")
        //val wordsDoc = HotWordRepository().getHotWords(FirebaseAuth.getInstance().currentUser!!.uid)
        wordsDoc.addOnSuccessListener { doc -> doc.changes.forEach { word -> hotWords.value!!.add(word.obj) } }
    }
}