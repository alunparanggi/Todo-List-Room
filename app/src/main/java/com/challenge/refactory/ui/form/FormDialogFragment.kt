package com.challenge.refactory.ui.form

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.challenge.refactory.App
import com.challenge.refactory.R
import com.challenge.refactory.databinding.FragmentFormDialogBinding
import com.challenge.refactory.ui.dashboard.TaskViewModel
import com.challenge.refactory.ui.dashboard.TaskViewModelFactory
import java.util.*

class FormDialogFragment(private val title: String, private val callback: () -> Unit)
    : DialogFragment() {
    private lateinit var binding: FragmentFormDialogBinding

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

    private val viewModel:TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as App).database.itemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFormDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitleDialog.text = title
        binding.btnCloseDialog.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnSave.setOnClickListener {
            addNewItem()
            dialog?.dismiss()
        }

        binding.btnDate.setOnClickListener { showDatePicker() }
        binding.btnStartTime.setOnClickListener { showTimePicker(binding.tvStartTime) }
        binding.btnFinishTime.setOnClickListener { showTimePicker(binding.tvFinishTime) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // request a window without the title
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    private fun getDateTimeCalendar(){
        val cal =  Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun showDatePicker(){
        getDateTimeCalendar()
        val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePicker, { _, year, month, dayOfMonth ->
            savedDay = dayOfMonth
            savedMonth = month + 1
            savedYear = year

            binding.tvDate.text = "$savedDay-$savedMonth-$savedYear"
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(textView: TextView){
        getDateTimeCalendar()
        val timePickerDialog = TimePickerDialog(requireContext(), R.style.DatePicker,  { time, hourOfDay, minute ->
            savedHour = hourOfDay
            savedMinute = minute

            textView.text = "$savedHour:$savedMinute"
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.etTaskTitle.text.toString(),
            binding.etTaskDesc.text.toString(),
            binding.tvDate.text.toString(),
            binding.tvStartTime.text.toString(),
            binding.tvFinishTime.text.toString(),
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.etTaskTitle.text.toString(),
                binding.etTaskDesc.text.toString(),
                binding.tvDate.text.toString(),
                binding.tvStartTime.text.toString(),
                binding.tvFinishTime.text.toString(),
            )
            dialog?.dismiss()
        }
    }
}