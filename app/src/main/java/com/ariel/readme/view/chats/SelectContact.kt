package com.ariel.readme.view.chats

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.R
import com.ariel.readme.view.settings.SettingActivity
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.repo.ModeledDocumentChange
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.databinding.FragmentChatListBinding
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.services.AuthService
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot

class SelectContact : AppCompatActivity() {
    lateinit var _binding: FragmentChatListBinding
    private val binding get() = _binding
    private val chats: MutableList<Chat> = mutableListOf()
    private lateinit var viewModel: SelectContactViewModel

    private val contactPermission = 1
    private val contactPicked = 2

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?
    ) {//From here click to select a contact
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbarChats))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentChatListBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(SelectContactViewModel::class.java)
        requestContactPermissions()

    }

    private fun ensurePermissionsAndInits(){
        if(hasContactPermissions()){
            initBindings()
            initAdapter()
            initListenChat()
        } else {
            requestContactPermissions()
        }
    }

    fun initBindings(){
        //Checks if the button was pressed
        binding.addContactButton.setOnClickListener {
            pickContact()
        }
        binding.setting2.setOnClickListener{
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

        }
    }

    fun initAdapter() {
        binding.ChatList.adapter = ChatListAdapter(chats, applicationContext)
        binding.ChatList.layoutManager = LinearLayoutManager(this)
    }

    private fun err(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    //to load the list
    private fun initListenChat() {
        // Should go via VM but will lead to duplicate code with no benefit
        RepositoryFactory.getChatRepository().listenOnChats(AuthService.getCurrentFirebaseUser()!!.uid, object : IGetChangedModels<Chat> {
            override fun onSuccess(d: List<ModeledDocumentChange<Chat>>, raw: QuerySnapshot?) {
                d.forEach { chat ->
                    when (chat.changeType) {//like swish
                        DocumentChange.Type.ADDED -> {
                            chat.obj.cid?.let {
                                chats.add(chat.obj)
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            chat.obj.cid?.let {
                               chats.remove(chat.obj)
                            }
                        }
                    }
                }
                _binding.ChatList.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(e: Exception) {//if failure error Toast
                err(e.message)
            }
        })


    }

    private fun pickContact() {//Selects a contact
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, contactPicked)
    }

    private fun requestContactPermissions() {//Requests permission to contact
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(this, permission, contactPermission)

    }

    private fun hasContactPermissions(): Boolean {//Checking contact permission
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == contactPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ensurePermissionsAndInits()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT)
                    .show()//אם הלקוח לא אשר
            }
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {//Treats after choosing a contact
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == contactPicked) {
                val cursor1: Cursor
                val uri: Uri? = data?.data//Get data from intent
                cursor1 = contentResolver.query(uri!!, null, null, null, null)!!
                if (cursor1.moveToFirst()) {//מקבלים פרטים ליצירת איש קשר
                    val contactNumber =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    viewModel.ensureChat(contactNumber)
                }
            } else {
                //Cancel contact selection
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}