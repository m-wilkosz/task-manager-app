package com.lsm.todo_app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.databinding.TaskItemBinding

class TaskListAdapter(private val homeViewModel: HomeViewModel) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallbackTask()) {

    class TaskViewHolder(private val binding: TaskItemBinding, private val homeViewModel: HomeViewModel) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: Task) = with(itemView) {
                    binding.item = item
                    binding.viewModel = homeViewModel
                }
            }

    class DiffCallbackTask : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding, homeViewModel)
    }
}