package com.ariel.readme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ariel.readme.databinding.ActivitySettingsBinding
import com.ariel.readme.profile.UserProfileActivity

class SettingActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.toolbar_setting))
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)       //TODO when main page is implemented


        //move to hot words activity
        _binding!!.hotWordsButton.setOnClickListener{
            val intent = Intent(this, HotWordsListActivity::class.java)
            startActivity(intent)
        }

        _binding!!.profileButton.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}