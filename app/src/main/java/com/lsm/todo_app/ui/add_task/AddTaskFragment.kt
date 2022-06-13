package com.lsm.todo_app.ui.add_task

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.lsm.todo_app.R
import com.lsm.todo_app.databinding.FragmentAddTaskBinding
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.notifyObserver
import java.util.*

class AddTaskFragment : BaseFragment<AddTaskViewModel>(AddTaskViewModel::class.java), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentAddTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addTaskViewModel =
            ViewModelProvider(this).get(AddTaskViewModel::class.java)

        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val prioritySpinner: Spinner = root.findViewById(R.id.prioritySpinner)
        prioritySpinner.onItemSelectedListener = this
        prioritySpinner.setGravity(Gravity.RIGHT)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priorities,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prioritySpinner.adapter = adapter
            prioritySpinner.setSelection(0)
        }

        val categorySpinner: Spinner = root.findViewById(R.id.categorySpinner)
        categorySpinner.onItemSelectedListener = this
        categorySpinner.setGravity(Gravity.RIGHT)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
            categorySpinner.setSelection(0)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this

        observeShowDatePickerRequest()
        observeShowTimePickerRequest()
    }

    private fun observeShowDatePickerRequest() {
        viewModel.showDatePickerRequest.observe(this.viewLifecycleOwner) {
            showDatePicker()
        }
    }

    private fun observeShowTimePickerRequest() {
        viewModel.showTimePickerRequest.observe(this.viewLifecycleOwner) {
            showTimePicker()
        }
    }

    private fun showDatePicker() {
        val title = resources.getString(R.string.select_date_hint)
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(title).build()
        datePicker.addOnPositiveButtonClickListener { value ->
            viewModel.task.value?.let { task ->
                task.date = Date(value)
                viewModel.task.notifyObserver()
            }
        }

        datePicker.show(this.parentFragmentManager, "DATE_PICKER_TAG")
    }

    private fun showTimePicker() {
        val title = resources.getString(R.string.select_time_hint)
        val timePicker = MaterialTimePicker
            .Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(title)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            viewModel.task.value?.let { task ->
                task.hour = timePicker.hour
                task.minute = timePicker.minute
                viewModel.task.notifyObserver()
            }
        }

        timePicker.show(this.parentFragmentManager, "TIME_PICKER_TAG")
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.task.value?.let { task ->
            val spinner: Spinner = parent as Spinner

            when (spinner.id) {
                R.id.prioritySpinner -> task.priority = parent.getItemAtPosition(pos).toString()
                R.id.categorySpinner -> task.category = parent.getItemAtPosition(pos).toString()
            }
            viewModel.task.notifyObserver()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}