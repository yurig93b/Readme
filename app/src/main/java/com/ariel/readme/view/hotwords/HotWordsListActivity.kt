package com.ariel.readme.view.hotwords

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.R
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.services.AuthService

class HotWordsListActivity : AppCompatActivity() {

    private var _vm: HotWordViewModel? = null
  
    private fun removeAll(){
        val clearAlert = AlertDialog.Builder(this)
        clearAlert.setTitle(getString(R.string.warning))
        clearAlert.setMessage(getString(R.string.clear_words_warning))
        clearAlert.setCancelable(false)
        clearAlert.setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, i: Int ->
            HotWordRepository().clearHotWords(AuthService.getCurrentFirebaseUser()!!.uid)
        }
        clearAlert.setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int -> }
        clearAlert.show()
    }

    private fun addWord(){
        val addAlert = AlertDialog.Builder(this)
        addAlert.setTitle(getString(R.string.add_new_word))
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(addAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton(getString(R.string.submit)) { dialog, which -> checkText(editText.text.toString()) }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int -> }
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
                    Toast.makeText(this,getString(R.string.invalid_word), Toast.LENGTH_LONG).show()
                    return
                }
            }
            Toast.makeText(this,"$text"+getString(R.string.word_was_added), Toast.LENGTH_LONG).show()
            _vm!!.addWord(text)
            return
        }
        Toast.makeText(this,getString(R.string.invalid_word), Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _vm = ViewModelProvider(this).get(HotWordViewModel::class.java)

        setContentView(R.layout.activity_hot_words_list)

        setSupportActionBar(findViewById(R.id.toolbar_hotWordsList))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    // set custom toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hot_words_toolbar_menu, menu)
        return true
    }

    // set toolbar click listeners
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when(itemView){
            R.id.add_word -> addWord()
            R.id.clear_list -> removeAll()
        }
        return false
    }
}