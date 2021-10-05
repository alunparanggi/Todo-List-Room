package com.challenge.refactory

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.challenge.refactory.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var date = ""
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnShowDatePicker.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun getDateTimeCalendar(){
        val cal =  Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun showDateTimePicker(){
        getDateTimeCalendar()
        val timePickerDialog = TimePickerDialog(this, R.style.DatePicker,  { time, hourOfDay, minute ->
            savedHour = hourOfDay
            savedMinute = minute


            binding.helo.text = "$savedDay-$savedMonth-$savedYear \n $savedHour:$savedMinute"
        }, hour, minute, true)

        val datePickerDialog = DatePickerDialog(this, R.style.DatePicker, { _, year, month, dayOfMonth ->
            savedDay = dayOfMonth
            savedMonth = month + 1
            savedYear = year

            getDateTimeCalendar()
            timePickerDialog.show()

        }, year, month, day)

        datePickerDialog.show()
    }

}