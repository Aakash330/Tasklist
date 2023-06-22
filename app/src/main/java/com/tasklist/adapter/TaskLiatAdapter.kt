package com.tasklist.adapter

import android.content.Context
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tasklist.R
import com.tasklist.databinding.TaskListItemViewBinding
import com.tasklist.model.TaskTable
import com.tasklist.util.AppConstent
import com.tasklist.util.StringLimit

class TaskLiatAdapter(
    val context: Context,
    val list: ArrayList<TaskTable>,
    val rvListner: RvListner
) : RecyclerView.Adapter<TaskLiatAdapter.TaskHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskListItemViewBinding.inflate(layoutInflater, parent, false)
        return TaskHolder(binding)

    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {

        holder.setData(list[position], position)
    }


    inner class TaskHolder(val binding: TaskListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(taskData: TaskTable, position: Int) {
            binding.tasktime.setText(taskData.time)

            if (AppConstent.ASSIGN == taskData.status || AppConstent.PENDING == taskData.status) {
                binding.taskname.setText(taskData.taskName.StringLimit(16))

                if (System.currentTimeMillis() > taskData.time_in_mili) {

                    binding.checked.setImageResource(R.drawable.unchecked)
                    binding.taskstatus.setText(R.string.pending)
                    taskData.status = AppConstent.PENDING
                    binding.taskname.setTextColor(context.resources.getColor(R.color.red))
                } else {
                    binding.taskname.setTextColor(context.resources.getColor(R.color.black))
                    binding.checked.setImageResource(R.drawable.unchecked)
                    binding.taskstatus.setText(R.string.assigned)
                }
            } else if (AppConstent.COMPLETED == taskData.status) {
                val html = "<b><del>${taskData.taskName.StringLimit(16)}</del></b>"
                binding.taskname.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                binding.taskname.setTextColor(context.resources.getColor(R.color.black))
                binding.checked.setImageResource(R.drawable.checked)
                binding.taskstatus.setText(R.string.completed)
            }
            binding.ivCross.setOnClickListener {
                rvListner.delete(position)
            }
            binding.checked.setOnClickListener {
                if (taskData.status == AppConstent.ASSIGN || taskData.status == AppConstent.PENDING) {
                    rvListner.changeStatus(position, AppConstent.COMPLETED)
                }

            }

        }

    }


}