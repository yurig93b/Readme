package com.ariel.readme.view.hotwords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.data.repo.ModeledDocumentChange
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.services.AuthService
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot


class HotWordViewModel() : ViewModel() {

    private val _hotWords : MutableLiveData<MutableList<String>> = MutableLiveData()
    val hotWords = _hotWords

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    var _listener : ListenerRegistration? = null

    fun listenOnHotWords(uid: String){
        _loading.value = true
        HotWordRepository().listenOnHotWords(uid, object : IGetChangedModels<HotWord> {
            override fun onSuccess(d: List<ModeledDocumentChange<HotWord>>, raw: QuerySnapshot?) {
                d.forEach {
                        word -> if(word.obj.word in _hotWords.value!!){ _hotWords.value!!.remove(word.obj.word)}
                else{ _hotWords.value!!.add(word.obj.word)}
                }
                _hotWords.value = _hotWords.value
                _hotWords.value!!.sort()
                _loading.value = false
            }
            override fun onFailure(e: Exception) {
            }
        })
    }

    fun addWord(word: String){
        val uid : String = AuthService.getCurrentFirebaseUser()!!.uid
        HotWordRepository().addHotWord(HotWord(null, uid, word), uid)
    }

    override fun onCleared() {
        super.onCleared()
        _listener?.apply {
            remove()
        }
    }

    init {
        _hotWords.value = mutableListOf()
    }
}