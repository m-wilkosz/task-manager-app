package com.lsm.todo_app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.lsm.todo_app.R
import com.lsm.todo_app.databinding.FragmentDashboardBinding
import com.lsm.todo_app.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<DashboardViewModel>(DashboardViewModel::class.java) {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var textDone: TextView? = null
    var textUndone: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textDone = binding.root.findViewById<View>(R.id.textViewDoneNumber) as TextView
        textUndone = binding.root.findViewById<View>(R.id.textViewUndoneNumber) as TextView
        textDone!!.text = "Number of all done tasks: " + (dashboardViewModel.doneTasksNum.value?.toString() ?: "0")
        textUndone!!.text = "Number of all undone tasks: " + (dashboardViewModel.undoneTasksNum.value?.toString() ?: "0")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this

        viewModel.reloadData()
        textDone!!.text = "Number of all done tasks: " + (viewModel.doneTasksNum.value?.toString() ?: "0")
        textUndone!!.text = "Number of all undone tasks: " + (viewModel.undoneTasksNum.value?.toString() ?: "0")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}