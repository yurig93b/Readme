package com.ariel.readme.voice

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.R
import com.ariel.readme.databinding.VoiceRecorderFragmentBinding
import java.io.IOException
import java.lang.Exception

class VoiceRecorderFragment : Fragment() {


    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    companion object {
        val ARG_BUNDLE_CHAT_ID = "chatId"
        val ARG_BUNDLE_USER_ID = "userId"

        fun newInstance() = VoiceRecorderFragment()
    }

    private lateinit var viewModel: VoiceRecorderViewModel
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var _binding: VoiceRecorderFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var _from: String
    private lateinit var _chat: String

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if (!permissionToRecordAccepted) {
            binding.recordButton.isEnabled = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleButtonDown(){
        //binding.recordButton.text = getString(R.string.button_recording)
        viewModel.record(activity?.applicationContext!!)
    }

    private fun handleButtonUp(){
       // binding.recordButton.text = getString(R.string.button_record)
        try {
            viewModel.stopRecording()
            viewModel.upload(_chat, _from)
        } catch (e: Exception){}

    }

    private fun registerLoadingObserver(){
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if(isLoading == true){
                binding.recordButton.isEnabled = false
               // binding.recordButton.text = getString(R.string.button_uploading)
            } else{
                binding.recordButton.isEnabled = true
               // binding.recordButton.text = getString(R.string.button_record)

            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerButtonClickListener(){
        binding.recordButton.setOnTouchListener(object : View.OnTouchListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    handleButtonDown()
                    return true
                } else if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
                    handleButtonUp()
                    return true
                }
                return false
            }
        })
    }

    private fun registerErrorObserver(){
        viewModel.error.observe(viewLifecycleOwner, { e ->
            Toast.makeText(
                context,
                e?.message,
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    private fun registerListeners() {
        registerLoadingObserver()
        registerButtonClickListener()
        registerErrorObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VoiceRecorderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VoiceRecorderViewModel::class.java)
        requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        _chat = requireArguments().getString(ARG_BUNDLE_CHAT_ID)!!
        _from = requireArguments().getString(ARG_BUNDLE_USER_ID)!!

        registerListeners()
    }
}