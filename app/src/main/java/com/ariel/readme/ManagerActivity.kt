package com.ariel.readme

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.data.viewmodel.ManagerViewModel
import com.ariel.readme.databinding.ActivityManagerBinding
import com.google.android.gms.tasks.Task

class ManagerActivity : AppCompatActivity() {

    private var _binding: ActivityManagerBinding? = null
    private val binding get() = _binding!!

    private var _vm: ManagerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityManagerBinding.inflate(layoutInflater)
        _vm = ViewModelProvider(this).get(ManagerViewModel::class.java)
        setContentView(binding.root)
        //setContentView(R.layout.activity_manager)

        _binding!!.banButton.setOnClickListener { banUser() }
        _binding!!.unbanButton.setOnClickListener { unbanUser() }
        _binding!!.addManButton.setOnClickListener { addManager() }
    }

    fun addManager(){
        val addAlert = AlertDialog.Builder(this)
        addAlert.setTitle("Please insert users' phone number")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(addAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Add") { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
                _vm!!.setTarget(editText.text.toString()).addOnCompleteListener {
                    val res : Task<Void>? = _vm!!.setManager()
                        if(res != null){
                            res?.addOnSuccessListener { Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.success_add_man),
                                Toast.LENGTH_SHORT
                            ).show() }!!.addOnFailureListener { Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.failure_add_man),
                                Toast.LENGTH_SHORT).show() }}
                        else{
                            Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.failure_add_man),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    fun banUser(){
        val banAlert = AlertDialog.Builder(this)
        banAlert.setTitle("You are banning a user!\n Please insert users' phone number")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(banAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Ban") { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
                _vm!!.setTarget(editText.text.toString()).addOnCompleteListener {
                    val res : Task<Void>? = _vm!!.setBanned()
                    if(res != null){
                        res?.addOnSuccessListener { Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.success_ban_user),
                            Toast.LENGTH_SHORT
                        ).show() }!!.addOnFailureListener { Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.failure_ban_user),
                            Toast.LENGTH_SHORT).show() }}
                    else{
                        Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.failure_ban_user),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    fun unbanUser(){
        val banAlert = AlertDialog.Builder(this)
        banAlert.setTitle("Please insert users' phone number")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(banAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Unban") { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
                _vm!!.setTarget(editText.text.toString()).addOnCompleteListener {
                    val res : Task<Void>? = _vm!!.setUnbanned()
                    if(res != null){
                        res?.addOnSuccessListener { Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.success_unban_user),
                            Toast.LENGTH_SHORT
                        ).show() }!!.addOnFailureListener { Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.failure_unban_user),
                            Toast.LENGTH_SHORT).show() }}
                    else{
                        Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.failure_unban_user),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }
}