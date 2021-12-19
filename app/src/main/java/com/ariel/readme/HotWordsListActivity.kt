package com.ariel.readme

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.databinding.ActivityHotWordsListBinding
import com.ariel.readme.services.AuthService

class HotWordsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotWordsListBinding

    private fun removeAll(){
        val clearAlert = AlertDialog.Builder(this)
        clearAlert.setTitle("Warning")
        clearAlert.setMessage("removing all your hot words is irreversible, are you sure you want to proceed?")
        clearAlert.setCancelable(false)
        clearAlert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            HotWordRepository().clearHotWords(AuthService.getCurrentFirebaseUser()!!.uid)
        }
        clearAlert.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
        clearAlert.show()
    }

    private fun addWord(){
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

    private fun checkText(text : String) {    //check for bad input mainly code injections
        if(text.length <= 24 && text != "") {
            val badChars: List<Char> = listOf(
                ' ', ';', '$', '|', '&',
                '(', ')', '[', ']', '{',
                '}', '<', '>', '\\', '/',
                '\n', '\t', '\r','+','-',
                '.','!','=','?'
            )
            for (char in badChars) {
                if (char in text) {
                    Toast.makeText(this,"invalid word", Toast.LENGTH_LONG).show()
                    return
                }
            }
            Toast.makeText(this,"$text was added", Toast.LENGTH_LONG).show()
            val uid : String = AuthService.getCurrentFirebaseUser()!!.uid
            HotWordRepository().addHotWord(HotWord(null, uid, text), uid)
            //HotWordRepository().addHotWord(HotWord(null, "1234" , text), "1234")
            return
        }
        Toast.makeText(this,"invalid word", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotWordsListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_hot_words_list)

        setSupportActionBar(findViewById(R.id.toolbar_hotWordsList))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imageAdd.setOnClickListener{ addWord() }
        binding.imageDelete.setOnClickListener{ removeAll() }
    }
}