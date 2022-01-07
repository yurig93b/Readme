package com.ariel.readme.view.profile

//import com.squareup.picasso.Picasso
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.R
import com.ariel.readme.view.chats.SelectContact
import com.ariel.readme.databinding.ActivityUserProfileBinding
import com.ariel.readme.factories.StorageFactory
import com.ariel.readme.factories.StoragePathFactory
import com.squareup.picasso.Picasso


class UserProfileActivity : AppCompatActivity() {

    companion object{
        val ARG_NEW_USER = "newUser"
    }

    private val _ERR_COULD_INIT_CAMERA = "Could not init camera."
    private val REQUEST_IMAGE_CAPTURE = 1

    private var _binding: ActivityUserProfileBinding? = null
    private var _vm: UserProfileViewModel? = null
    private val binding get() = _binding!!

    private var isNewUser: Boolean = false

    private fun updateSaveButtonStatus() {
        binding.save.isEnabled = _vm!!.errors.value!!.size == 0 && _vm!!.loading.value == false
    }

    private fun observeLoading() {
        _vm!!.loading.observe(this, { isLoading ->
            if (isLoading) {
                binding.imageView.isEnabled = false
                binding.firstName.isEnabled = false
                binding.lastName.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.imageView.isEnabled = true
                binding.firstName.isEnabled = true
                binding.lastName.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
            }
            updateSaveButtonStatus()
        })
    }

    private fun listenProfileChangeClick() {
        binding.imageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dispatchTakePictureIntent()
            }
        })
    }

    private fun observeValidationErrors() {
        _vm!!.errors.observe(this, { errors ->
            updateSaveButtonStatus()
        })
    }

    private fun observeUserUpdates() {
        _vm!!.user.observe(this, { user ->
            binding.firstName.setText(user.firstName)
            binding.lastName.setText(user.lastName)

            StorageFactory.getStorage()
                .getDownloadUrl(StoragePathFactory.getProfilePicPath(user))
                .addOnSuccessListener { url ->
                    Picasso.get().load(url).into(binding.imageView)
                }
        })
    }

    private fun observeInputs() {
        binding.firstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    binding.firstName.error = getString(R.string.err_first_name_empty)
                    _vm!!.errors.value!!.add(binding.firstName.id)
                    _vm!!.errors.value = _vm!!.errors.value
                } else {
                    _vm!!.errors.value!!.remove(binding.firstName.id)
                    _vm!!.errors.value = _vm!!.errors.value
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        binding.lastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    binding.lastName.error = getString(R.string.err_last_name_empty)
                    _vm!!.errors.value!!.add(binding.lastName.id)
                    _vm!!.errors.value = _vm!!.errors.value
                } else {
                    _vm!!.errors.value!!.remove(binding.lastName.id)
                    _vm!!.errors.value = _vm!!.errors.value
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    private fun listenSaveClick() {
        binding.save.setOnClickListener { v ->
            val task = _vm!!.saveProfile(
                binding.firstName.text.toString(),
                binding.lastName.text.toString()
            )
            task?.addOnSuccessListener {
                Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.success_save_profile),
                    Toast.LENGTH_SHORT
                ).show()

                if(isNewUser){
                    val intent = Intent(this, SelectContact::class.java)
                    intent.setFlags(intent.flags or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }

            }?.addOnFailureListener {
                Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.err_failed_profile_save),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun registerListeners() {
        observeLoading()
        listenProfileChangeClick()
        listenSaveClick()
        observeValidationErrors()
        observeUserUpdates()
        observeInputs()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(getApplicationContext(), _ERR_COULD_INIT_CAMERA, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            binding.imageView.setImageBitmap(imageBitmap)
            _vm!!.updatePublicPic(imageBitmap).addOnFailureListener {
                Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.err_public_image_upload),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserProfileBinding.inflate(layoutInflater)
        _vm = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        intent.extras?.apply {
            isNewUser = this.getBoolean(ARG_NEW_USER, false)
        }
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        registerListeners()
        _vm!!.loadUser()
    }
}