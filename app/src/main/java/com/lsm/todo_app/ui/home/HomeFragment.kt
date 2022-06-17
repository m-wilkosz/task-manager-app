package com.lsm.todo_app.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.lsm.todo_app.R
import com.lsm.todo_app.databinding.FragmentHomeBinding
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.notifyObserver
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>(HomeViewModel::class.java),
    AdapterView.OnItemSelectedListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val categorySpinner: Spinner = root.findViewById(R.id.categorySpinner)
        categorySpinner.onItemSelectedListener = this
        categorySpinner.setGravity(Gravity.RIGHT)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            R.layout.spinner_no_text_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
            categorySpinner.setSelection(0)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this

        observeShowDatePickerRequest()

        var adapter = TaskListAdapter(this.viewModel)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.recycleView.adapter = adapter

        binding.viewModel?.taskList?.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        val toolbar = binding.root.findViewById<View>(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar?.setTitle("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeShowDatePickerRequest() {
        viewModel.showDatePickerRequest.observe(this.viewLifecycleOwner) {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val title = resources.getString(R.string.select_date_hint)
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(title).build()
        datePicker.addOnPositiveButtonClickListener { value ->
            viewModel.choice.value?.let { choice ->
                choice.date = Date(value)
                viewModel.choice.notifyObserver()
            }
        }

        datePicker.show(this.parentFragmentManager, "DATE_PICKER_TAG")
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.choice.value?.let { choice ->
            choice.category = parent.getItemAtPosition(pos).toString()
            viewModel.choice.notifyObserver()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}