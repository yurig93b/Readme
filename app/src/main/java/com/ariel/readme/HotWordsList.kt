package com.ariel.readme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class HotWordsList : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_words_list)

        //val recycler : RecyclerView = findViewById(R.id.word_list)

        setSupportActionBar(findViewById(R.id.toolbar_hotWordsList))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}