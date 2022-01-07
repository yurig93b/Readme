package com.ariel.readme.view.manager

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.R
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
    private var type: Boolean = true

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
        _binding!!.switchButton.setOnClickListener {  //switch type of graph
            if(type){
                _binding!!.switchButton.text = getString(R.string.messages_per_round)
            }
            else{
                _binding!!.switchButton.text = getString(R.string.active_users)
            }
            type = !type
            refresh()
        }
    }

    private fun observeLoading() {
        _vm!!.loading.observe(this, { isLoading ->
            if (isLoading) {
                binding.banButton.isEnabled = false
                binding.unbanButton.isEnabled = false
                binding.addManButton.isEnabled = false
                binding.refreshButton.isEnabled = false
                binding.graph.isEnabled = false
                binding.switchButton.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.banButton.isEnabled = true
                binding.unbanButton.isEnabled = true
                binding.addManButton.isEnabled = true
                binding.refreshButton.isEnabled = true
                binding.graph.isEnabled = true
                binding.switchButton.isEnabled = true
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

    // listen for graph changes
    private fun initListeners(){
        _vm!!.loadedDataPointAU!!.observe(this, { data ->
            val series = LineGraphSeries(data)
            if(type){
                initGraph(series)
            }
        })
        _vm!!.loadedDataPointMPR!!.observe(this, { data ->
            val series = LineGraphSeries(data)
            if(!type){
                initGraph(series)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refresh(){
        _vm!!.loadStatistics()
    }

    // set graph's options and check for failure
    private fun initGraph(series: LineGraphSeries<DataPoint>){
        binding.graph.removeAllSeries()
        series.setAnimated(true)
        if(!type){ series.setColor(Color.argb(255, 255, 60, 60))}
        binding.graph.addSeries(series)
        binding.graph.viewport.isXAxisBoundsManual = true
        binding.graph.viewport.isScalable = true
        if(_vm!!.loadedDataPointAU.value!!.size == 1 && _vm!!.loadedDataPointAU.value!![0].x == 0.0 && _vm!!.loadedDataPointAU.value!![0].y == 0.0){
            Toast.makeText(applicationContext,
                getString(R.string.failure_load_graph),
                Toast.LENGTH_SHORT).show()
        }
    }
}