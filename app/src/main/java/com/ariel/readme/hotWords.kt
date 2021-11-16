package com.ariel.readme

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class hotWords : AppCompatActivity() {

    private fun removeAllAlert(){
        val clearAlert = AlertDialog.Builder(this)
        clearAlert.setTitle("Warning")
        clearAlert.setMessage("removing all your hot words is irreversible, are you sure you want to proceed?")
        clearAlert.setCancelable(false)
        clearAlert.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int -> })
        clearAlert.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        clearAlert.show()
    }

    private fun addWord(){
        val addAlert = AlertDialog.Builder(this)
        addAlert.setTitle("add new word")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        addAlert.setView(dialogLayout)
        addAlert.setCancelable(false)
        addAlert.setPositiveButton("Submit", { dialogInterface: DialogInterface, i: Int -> })
        addAlert.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        addAlert.show()
    }

    fun removeWord(){
        val removeAlert = AlertDialog.Builder(this)
        removeAlert.setTitle("remove word")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        removeAlert.setView(dialogLayout)
        removeAlert.setCancelable(false)
        removeAlert.setPositiveButton("Remove", { dialogInterface: DialogInterface, i: Int -> })
        removeAlert.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        removeAlert.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_words)

        setSupportActionBar(findViewById(R.id.toolbar_hotWords))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val addWordButton : Button = findViewById(R.id.add_words)
        val removeWordButton : Button = findViewById(R.id.remove_words)
        val removeAllButton : Button = findViewById(R.id.clear_all_words)

        addWordButton.setOnClickListener{addWord()}
        removeWordButton.setOnClickListener{removeWord()}
        removeAllButton.setOnClickListener{ removeAllAlert()}
    }
}