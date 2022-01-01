package com.ariel.readme

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.data.viewmodel.ManagerViewModel
import com.ariel.readme.databinding.ActivityManagerBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*

class ManagerActivity : AppCompatActivity() {

    private var _binding: ActivityManagerBinding? = null
    private val binding get() = _binding!!

    private var _vm: ManagerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityManagerBinding.inflate(layoutInflater)
        _vm = ViewModelProvider(this).get(ManagerViewModel::class.java)
        setContentView(binding.root)

        _binding!!.graph.viewport.setMaxX(23.0)
        _binding!!.graph.viewport.isXAxisBoundsManual = true
        _binding!!.graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
        //_binding!!.graph.gridLabelRenderer.numHorizontalLabels = 6

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
        addAlert.setTitle("Please insert users' phone number")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(addAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Add") { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
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
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun banUser(){
        observeLoading()
        val banAlert = AlertDialog.Builder(this)
        banAlert.setTitle("You are banning a user!\nPlease insert users' phone number")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        val editText : EditText = dialogLayout.findViewById(R.id.edit_text_add)
        with(banAlert){
            setView(dialogLayout)
            setCancelable(false)
            setPositiveButton("Ban") { dialog, which -> _vm!!.checkUser().addOnCompleteListener {
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
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun unbanUser(){
        observeLoading()
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
            setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            show()
        }
    }

    private fun refresh(){
        val start = " 00:00:00"
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date()) + start
        //val simpleDateFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
        val time = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDate)
        //val time = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("30-12-2021 00:00:00")
        _vm!!.setGraph(Timestamp(time)).addOnCompleteListener {
            val array = _vm!!.dataPoints.value!!
            val dp: Array<DataPoint> = arrayOf(DataPoint(array[0][0],array[0][1]),
                DataPoint(array[1][0],array[1][1]),DataPoint(array[2][0],array[2][1]),DataPoint(array[3][0],array[3][1]),
                DataPoint(array[4][0],array[4][1]),DataPoint(array[5][0],array[5][1]),DataPoint(array[6][0],array[6][1]),
                DataPoint(array[7][0],array[7][1]),DataPoint(array[8][0],array[8][1]),DataPoint(array[9][0],array[9][1]),
                DataPoint(array[10][0],array[10][1]),DataPoint(array[11][0],array[11][1]),DataPoint(array[12][0],array[12][1]),
                DataPoint(array[13][0],array[13][1]),DataPoint(array[14][0],array[14][1]),DataPoint(array[15][0],array[15][1]),
                DataPoint(array[16][0],array[16][1]),DataPoint(array[17][0],array[17][1]),DataPoint(array[18][0],array[18][1]),
                DataPoint(array[19][0],array[19][1]),DataPoint(array[20][0],array[20][1]),DataPoint(array[21][0],array[21][1]),
                DataPoint(array[22][0],array[22][1]),DataPoint(array[23][0],array[23][1]))
            val series = LineGraphSeries(dp)
            initGraph(series)
        }
    }

    private fun initGraph(series: LineGraphSeries<DataPoint>){
        binding.graph.removeAllSeries()
        series.setAnimated(true)
        binding.graph.addSeries(series)
    }
}