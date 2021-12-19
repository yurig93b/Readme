package com.ariel.readme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.toolbar_setting))
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)       //TODO when main page is implemented


        //move to hot words activity
        val hotWordsButton : Button = findViewById(R.id.hot_words_button)
        hotWordsButton.setOnClickListener{
            val intent = Intent(this, HotWordsListActivity::class.java)
            startActivity(intent)
        }
    }
}