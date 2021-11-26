package com.ariel.readme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.repo.HotWordRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class HotWordsList : AppCompatActivity() {

    private val words : MutableList<HotWord> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_words_list)

        setSupportActionBar(findViewById(R.id.toolbar_hotWordsList))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //val wordsDoc  = HotWordRepository().getHotWords(FirebaseAuth.getInstance().currentUser!!.uid)
        val wordsDoc : Task<QuerySnapshot> = HotWordRepository().getHotWords("1234")
        wordsDoc.addOnSuccessListener { w -> w.documents }
    }
}