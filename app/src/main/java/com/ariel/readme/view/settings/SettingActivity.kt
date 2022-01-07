package com.ariel.readme.view.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.view.manager.ManagerViewModel
import com.ariel.readme.databinding.ActivitySettingsBinding
import com.ariel.readme.view.hotwords.HotWordsListActivity
import com.ariel.readme.view.manager.ManagerActivity
import com.ariel.readme.view.profile.UserProfileActivity

class SettingActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!
    private var _vm: ManagerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        _vm = ViewModelProvider(this).get(ManagerViewModel::class.java)
        setContentView(binding.root)

        setSupportActionBar(_binding!!.toolbarSetting)

        listenToManager()

        //move to hot words activity
        _binding!!.hotWordsButton.setOnClickListener{
            val intent = Intent(this, HotWordsListActivity::class.java)
            startActivity(intent)
        }

        _binding!!.profileButton.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        _binding!!.managerButton.setOnClickListener {
            val intent = Intent(this, ManagerActivity::class.java)
            startActivity(intent)
        }
    }

    //check if user is manager
    private fun listenToManager(){
        _vm!!.checkUser()
        _vm!!.user!!.observe(this, { user ->
            if(user.manager){ _binding!!.managerButton.visibility = View.VISIBLE}
            else { _binding!!.managerButton.visibility = View.INVISIBLE}
        })
    }
}