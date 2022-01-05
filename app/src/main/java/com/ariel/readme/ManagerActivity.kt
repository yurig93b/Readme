package com.ariel.readme

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.data.viewmodel.ManagerViewModel
import com.ariel.readme.databinding.ActivityManagerBinding
import com.google.android.gms.tasks.Task
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat

class ManagerActivity : AppCompatActivity() {

    private var _binding: ActivityManagerBinding? = null
    private val binding get() = _binding!!

    private var _vm: ManagerViewModel? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityManagerBinding.inflate(layoutInflater)
        _vm = ViewModelProvider(this).get(ManagerViewModel::class.java)
        setContentView(binding.root)

        setSupportActionBar(_binding!!.toolbarManager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _binding!!.graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
        _binding!!.graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this,SimpleDateFormat("HH:mm"))

        initListeners()
        refresh() // set the graph

        _binding!!.banButton.setOnClickListener { banUser() }
        _binding!!.unbanButton.setOnClickListener { unbanUser() }
        _binding!!.addManButton.setOnClickListener { addManager() }
        _binding!!.refreshButton.setOnClickListener { refresh() }
    }

    private fun observeLoading() {
        _vm!!.loading.observe(this, { isLoading ->
            if (isLoading) {
                binding.banButton.isEnabled = false
                binding.unbanButton.isEnabled = false
                binding.addManButton.isEnabled = false
                binding.refreshButton.isEnabled = false
                binding.graph.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.banButton.isEnabled = true
                binding.unbanButton.isEnabled = true
                binding.addManButton.isEnabled = true
                binding.refreshButton.isEnabled = true
                binding.graph.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun addManager(){
        observeLoading()
        val addAlert = AlertDialog.Builder(this)
        addAlert.setTitle(getString(R.string.phone_request))
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(addAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton(getString(R.string.add)) { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
                _vm!!.setTarget(editText.text.toString()).addOnCompleteListener {
                    val res : Task<Void>? = _vm!!.setManager()
                        if(res != null){
                            res.addOnSuccessListener { Toast.makeText(
                                applicationContext,
                                getString(R.string.success_add_man),
                                Toast.LENGTH_SHORT
                            ).show() }.addOnFailureListener { Toast.makeText(
                                applicationContext,
                                getString(R.string.failure_add_man),
                                Toast.LENGTH_SHORT).show() }}
                        else{
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.failure_add_man),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun banUser(){
        observeLoading()
        val banAlert = AlertDialog.Builder(this)
        banAlert.setTitle(getString(R.string.ban_warning))
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(banAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton(getString(R.string.ban)) { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
                _vm!!.setTarget(editText.text.toString()).addOnCompleteListener {
                    val res : Task<Void>? = _vm!!.setBanned()
                    if(res != null){
                        res.addOnSuccessListener { Toast.makeText(
                            applicationContext,
                            getString(R.string.success_ban_user),
                            Toast.LENGTH_SHORT
                        ).show() }.addOnFailureListener { Toast.makeText(
                            applicationContext,
                            getString(R.string.failure_ban_user),
                            Toast.LENGTH_SHORT).show() }}
                    else{
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.failure_ban_user),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun unbanUser(){
        observeLoading()
        val banAlert = AlertDialog.Builder(this)
        banAlert.setTitle(getString(R.string.phone_request))
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(banAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton(getString(R.string.unban)) { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
                _vm!!.setTarget(editText.text.toString()).addOnCompleteListener {
                    val res : Task<Void>? = _vm!!.setUnbanned()
                    if(res != null){
                        res.addOnSuccessListener { Toast.makeText(
                            applicationContext,
                            getString(R.string.success_unban_user),
                            Toast.LENGTH_SHORT
                        ).show() }.addOnFailureListener { Toast.makeText(
                            applicationContext,
                            getString(R.string.failure_unban_user),
                            Toast.LENGTH_SHORT).show() }}
                    else{
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.failure_unban_user),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun initListeners(){
        _vm!!.loadedDataPoint!!.observe(this, { data ->
            val series = LineGraphSeries(data)
            initGraph(series)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refresh(){
        _vm!!.loadStatistics()
    }

    private fun initGraph(series: LineGraphSeries<DataPoint>){
        binding.graph.removeAllSeries()
        series.setAnimated(true)
        binding.graph.addSeries(series)
        binding.graph.viewport.isXAxisBoundsManual = true
        binding.graph.viewport.isScalable = true
        if(_vm!!.loadedDataPoint.value!!.size == 1 && _vm!!.loadedDataPoint.value!![0].x == 0.0 && _vm!!.loadedDataPoint.value!![0].y == 0.0){
            Toast.makeText(applicationContext,
                getString(R.string.failure_load_graph),
                Toast.LENGTH_SHORT).show()
        }
    }
}