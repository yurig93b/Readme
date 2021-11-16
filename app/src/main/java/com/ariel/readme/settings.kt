package com.ariel.readme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.toolbar_setting))
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val hotWordsButton : Button = findViewById(R.id.hot_words_button)
        hotWordsButton.setOnClickListener{
            val intent = Intent(this, hotWords::class.java)
            startActivity(intent)
        }
    }
}