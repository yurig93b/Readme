package com.ariel.readme.view.sender

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.R
import com.ariel.readme.databinding.SenderFragmentBinding

class SenderFragment : Fragment() {


    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    companion object {
        val ARG_BUNDLE_CHAT_ID = "chatId"
        val ARG_BUNDLE_USER_ID = "userId"

        fun newInstance() = SenderFragment()
    }

    private lateinit var viewModel: SenderFragmentViewModel
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var _binding: SenderFragmentBinding? = null
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleVoiceButtonDown() {
        binding.recordButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(android.R.drawable.radiobutton_off_background)
            , null, null, null)

        viewModel.record(activity?.applicationContext!!)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleVoiceButtonUp() {
        binding.recordButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(android.R.drawable.stat_notify_sync),null,null,null)

        try {
            viewModel.stopRecording()
            viewModel.upload(_chat, _from)
        } catch (e: Exception) {
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(drawable : Int): Drawable? {
        val d: Drawable? = context?.getDrawable(drawable)
        d?.let{
            DrawableCompat.wrap(d);
            DrawableCompat.setTint(d, resources.getColor(R.color.white))
            return d
        }
        return null
    }

    private fun registerLoadingObserver() {
        viewModel.loading_voice.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true) {
                binding.recordButton.isEnabled = false
                binding.recordButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(android.R.drawable.stat_notify_sync),null,null,null)
            } else {
                binding.recordButton.isEnabled = true
                binding.recordButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(android.R.drawable.ic_btn_speak_now), null,null,null)

            }
        })
    }


    private fun handleSendButton() {
        binding.textInputLayout.editText?.text?.let {
            if (it.isEmpty()) {
                return
            }
            viewModel.sendMessage(_chat, _from, it.toString())
            binding.textInputLayout.editText?.text = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerButtonClickListener() {
        binding.recordButton.setOnTouchListener(object : View.OnTouchListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    handleVoiceButtonDown()
                    return true
                } else if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
                    handleVoiceButtonUp()
                    return true
                }
                return false
            }
        })

        binding.sendButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                handleSendButton()
            }
        })

    }

    private fun registerErrorObserver() {
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
        _binding = SenderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SenderFragmentViewModel::class.java)
        val chatId = arguments?.getString(ARG_BUNDLE_CHAT_ID)
        val userId = arguments?.getString(ARG_BUNDLE_USER_ID)

        chatId?.let {
            userId?.let {
                requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)
                _chat = chatId
                _from = userId
                registerListeners()
            }
        }

    }
}