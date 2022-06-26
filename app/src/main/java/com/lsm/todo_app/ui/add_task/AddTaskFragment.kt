package com.lsm.todo_app.ui.add_task

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.lsm.todo_app.R
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.databinding.FragmentAddTaskBinding
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.notifyObserver
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
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

        val radioGroup : RadioGroup = binding.root.findViewById<View>(R.id.radioGroupFrequency) as RadioGroup
        radioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                val checked = when (checkedId) {
                    R.id.radio_button_once -> "once"
                    R.id.radio_button_daily -> "daily"
                    R.id.radio_button_weekly -> "weekly"
                    R.id.radio_button_monthly -> "monthly"
                    R.id.radio_button_yearly -> "yearly"
                    else -> ""
                }

                viewModel.task.value?.let { task ->
                    task.frequency = checked
                    viewModel.task.notifyObserver()
                }
            }
        })

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
        observeSetAlarmRequest()
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

    private fun observeSetAlarmRequest() {
        viewModel.setAlarmRequest.observe(this.viewLifecycleOwner) {
            setAlarm(it)
        }
    }

    private fun setAlarm(task: Task) {
        createNotificationChannel()
        Log.d("Alarm", "Call setAlarm from AddTaskFragment")
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, BroadcastAlarm::class.java)
        intent.putExtra("title", task.title)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            clear()
            set(Calendar.YEAR, task.year)
            set(Calendar.MONTH, task.month - 1)
            set(Calendar.DAY_OF_MONTH, task.day)
            set(Calendar.HOUR_OF_DAY, task.hour)
            set(Calendar.MINUTE, task.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.AM_PM, Calendar.PM)
        }

        val interval : Long = when (task.frequency) {
            "once" -> Long.MAX_VALUE
            "daily" -> AlarmManager.INTERVAL_DAY
            "weekly" -> AlarmManager.INTERVAL_DAY * 7
            "monthly" -> Long.MAX_VALUE
            else -> {Long.MAX_VALUE}
        }

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            interval,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("0", "channel_name", importance)
            channel.description = "channel desc"

            val a : NotificationManager? = null
            val notificationManager = NotificationManagerCompat.from(this.requireContext())
            notificationManager.createNotificationChannel(channel)
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
            val textField = binding.root.findViewById<View>(R.id.outlinedTextFieldDateText) as TextInputEditText
            val format = SimpleDateFormat("dd/MM/yyyy")
            textField.setText(format.format(Date(value)))
        }

        datePicker.show(this.parentFragmentManager, "DATE_PICKER_TAG")
    }

    private fun showTimePicker() {
        val title = resources.getString(R.string.select_time_hint)
        val timePicker = MaterialTimePicker
            .Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(0)
            .setMinute(0)
            .setTitleText(title)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            viewModel.task.value?.let { task ->
                task.hour = timePicker.hour
                task.minute = timePicker.minute
                viewModel.task.notifyObserver()
            }
            val textField = binding.root.findViewById<View>(R.id.outlinedTextFieldTimeText) as TextInputEditText
            if (timePicker.minute < 10)
                textField.setText("${timePicker.hour}:0${timePicker.minute}")
            else
                textField.setText("${timePicker.hour}:${timePicker.minute}")
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