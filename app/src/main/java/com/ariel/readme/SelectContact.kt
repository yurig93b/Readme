package com.ariel.readme

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
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.data.viewmodel.SelectContactViewModel
import com.ariel.readme.databinding.ActivitySelectContactBinding

class SelectContact : AppCompatActivity() {
    lateinit var binding: ActivitySelectContactBinding
    private lateinit var viewModel: SelectContactViewModel
    private val Contact_Permission = 1//הרשאה
    private val Contact_Pick = 2

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {//From here click to select a contact
        super.onCreate(savedInstanceState)
        binding = ActivitySelectContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SelectContactViewModel::class.java)
        viewModel.checktest()
        //Checks if the button was pressed
        binding.addCon.setOnClickListener {
            if (check()) {
                //allow
                pickContact()
            } else {
                //not
                requestContact()
            }

        }
    }
    private fun pickContact() {//Selects a contact
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, Contact_Pick)
    }
    private fun requestContact() {//Requests permission to contact
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(this, permission, Contact_Permission)

    }
    private fun check(): Boolean {//Checking contact permission
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

        if (requestCode == Contact_Permission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContact()
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
            if (requestCode == Contact_Pick) {
                binding.listContacts.text = ""
                val cursor1: Cursor
                val cursor2: Cursor?
                val uri: Uri? = data?.data//Get data from intent
                cursor1 = contentResolver.query(uri!!, null, null, null, null)!!
                if (cursor1.moveToFirst()) {//מקבלים פרטים ליצירת איש קשר
                    val contactId =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
                    val contactName =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactNumber =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val contactPhoto =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val idResult =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val idResultHold = idResult.toInt()


                    viewModel.ensureChat(contactNumber)

                    //binding.listContacts.append("\nId:$contactId")
                    //What is written in the text defines the details
//                    binding.listContacts.append("\nName:$contactName")
//                    binding.listContacts.append("\nPhone: $contactNumber")
                    // binding.listContacts.append("\nImage:$contactPhoto")
                    //מגדיר תמונה
//                        if(contactPhoto!=null){
//                            binding.photoo.setImageURI(Uri.parse(contactPhoto))      //If there is a picture define it
//                        }
//                        else{
//                            binding.photoo.setImageResource(R.drawable.userpro)//If there is no image choose a dipulatory image
//
//                        }
// // Check if the contact has a phone number
//                        if(idResultHold==1){
//                            cursor2=contentResolver.query(
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" "+contactId,null,
//                                null)
//                        //If the same contact has more than one number
//                            while(cursor2!!.moveToNext()){
//                                //get phone num
//                                val contactNum=cursor2.getString(cursor2.getColumnIndex(
//                                    ContactsContract.CommonDataKinds.Phone.NUMBER))
//                                //SET THE NUM PHONE
//                                binding.listContacts.append("\nPhone: $contactNum")
//                            }
//                            cursor2.close()
//
//                        }
//                        cursor1.close()
//                    }

                }
            } else {
                //Cancel contact selection
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()

            }
        }
    }
}