package com.ariel.readme

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class hotWords : AppCompatActivity() {

    private fun removeAll(){   //TODO: empty the hot word file
        val clearAlert = AlertDialog.Builder(this)
        clearAlert.setTitle("Warning")
        clearAlert.setMessage("removing all your hot words is irreversible, are you sure you want to proceed?")
        clearAlert.setCancelable(false)
        clearAlert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int -> }
        clearAlert.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
        clearAlert.show()
    }

    private fun addWord(){    //TODO add the word to hot word file
        val addAlert = AlertDialog.Builder(this)
        addAlert.setTitle("add new word")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(addAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Submit") { dialog, which -> checkText(editText.text.toString()) }
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun checkText(text : String){    //check for bad input mainly code injections
        if(text.length <= 24 && text != "") {
            val badChars: List<Char> = listOf(
                ' ', ';', '$', '|', '&',
                '(', ')', '[', ']', '{',
                '}', '<', '>', '\\', '/',
                '\n', '\t', '\r','+','-',
                '#','$'
            )
            for (char in badChars) {
                if (char in text) {
                    Toast.makeText(this,"invalid word", Toast.LENGTH_LONG).show()
                    return
                }
            }
            Toast.makeText(this,"$text was added", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(this,"invalid word", Toast.LENGTH_LONG).show()
    }

    private fun removeWord(){   //TODO remove a word from hot word file
        val removeAlert = AlertDialog.Builder(this)
        removeAlert.setTitle("remove word")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(removeAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Remove") { dialogInterface: DialogInterface, i: Int -> }
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_words)

        setSupportActionBar(findViewById(R.id.toolbar_hotWords))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val addWordButton : Button = findViewById(R.id.add_words)
        val removeWordButton : Button = findViewById(R.id.remove_words)
        val viewAllButton : Button = findViewById(R.id.view_words)
        val removeAllButton : Button = findViewById(R.id.clear_all_words)

        addWordButton.setOnClickListener{addWord()}
        removeWordButton.setOnClickListener{removeWord()}
        viewAllButton.setOnClickListener{
            val intent = Intent(this, HotWordsList::class.java)
            startActivity(intent)
        }
        removeAllButton.setOnClickListener{ removeAll() }
    }

}