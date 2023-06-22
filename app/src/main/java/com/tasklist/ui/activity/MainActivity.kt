package com.tasklist.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.tasklist.MyApplication
import com.tasklist.R
import com.tasklist.adapter.RvListner
import com.tasklist.adapter.TaskLiatAdapter
import com.tasklist.databinding.ActivityMainBinding
import com.tasklist.databinding.CustomDialogViewBinding
import com.tasklist.model.TaskTable
import com.tasklist.util.AppConstent
import com.tasklist.util.toToast
import com.tasklist.viewmodel.TaskViewModel
import com.tasklist.viewmodel.TaskViewModelFactory
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity(), RvListner {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskList: ArrayList<TaskTable>
    private lateinit var adapter: TaskLiatAdapter
    @Inject
    lateinit var taskViewModelFactory: TaskViewModelFactory
    lateinit var taskViewModel: TaskViewModel
    private var currentPostion: Int = 0
    private var isAlreadyReversed=false
    private val launchIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data?.getStringExtra(AppConstent.TASK_DATA)
                val taskTable = Gson().fromJson(data, TaskTable::class.java)
                binding.notask.visibility=View.INVISIBLE
                taskList.add(0, taskTable)
                adapter.notifyDataSetChanged()
                binding.rvtasklist.scrollToPosition(0)

            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setObserver()
        getAllData()
        initClick()
    }


    private fun init() {
        (application as MyApplication).applicationComponent.inject(this)
        taskViewModel = ViewModelProvider(this, taskViewModelFactory)[TaskViewModel::class.java]
        taskList = ArrayList()
        adapter = TaskLiatAdapter(this, taskList, this)
        binding.rvtasklist.adapter = adapter

        //

    }

    /**
     * this method also used for sort the data according to the date
     */
    private fun getAllData(date: String = "") {

        if (date == "") {
            taskViewModel.getAllTask()
        } else {
            taskViewModel.getByFilter(date)
        }
    }

    private fun setObserver() {
        taskViewModel.onError.observe(this) {
            this.toToast(it)
        }
        taskViewModel.allTaskListData.observe(this) {
            taskList.clear()
            taskList.addAll(it)
            adapter.notifyDataSetChanged()
            if (taskList.size==0)
                binding.notask.visibility=View.VISIBLE
        }

        taskViewModel.deleteObserver.observe(this) {
            this.toToast(getString(R.string.task_removed))
            taskList.removeAt(currentPostion)
            adapter.notifyDataSetChanged()
            if (taskList.size==0)
                binding.notask.visibility=View.VISIBLE
        }
        taskViewModel.updateObserver.observe(this) {
            taskList[currentPostion].status = AppConstent.COMPLETED
            adapter.notifyItemChanged(currentPostion)
        }


    }

    private fun initClick() {

        binding.addtask.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTask::class.java)
            launchIntent.launch(intent)
        }
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivmenu.setOnClickListener {
            ShowMenu()
        }
    }


    override fun changeStatus(pos: Int, status: String) {
        currentPostion = pos
        taskViewModel.updateStatus(status, taskList[pos].id)

    }

    override fun delete(pos: Int) {
        currentPostion = pos
        showDeleteAlert()

    }


    private fun ShowMenu() {
        val popupMenu = PopupMenu(this@MainActivity, binding.ivmenu)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                when (p0?.itemId) {
                    R.id.sortbydate -> {
                        showDatePicker()
                        return true
                    }
                    R.id.newtoold ->{
                        if (!isAlreadyReversed)
                        {
                            taskList.reverse()
                            isAlreadyReversed=true
                            adapter.notifyDataSetChanged()
                        }

                    }
                    R.id.oldtonew ->{
                        if (isAlreadyReversed)
                        {
                            taskList.reverse()
                            isAlreadyReversed=false
                            adapter.notifyDataSetChanged()
                        }

                    }
                }
                return false
            }

        })
        popupMenu.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val myCalendar: Calendar = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { p0, year, monthOfYear, dayOfMonth ->
                val pickedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                getAllData(pickedDate.toString().trim())
            }

        val datePicker = DatePickerDialog(
            this,
            date,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
        datePicker.datePicker.maxDate = System.currentTimeMillis()
    }

   private fun showDeleteAlert()
    {
        val dialogBinding = CustomDialogViewBinding.inflate(layoutInflater)
        val dialog =
            MaterialAlertDialogBuilder(
                this,
                R.style.MyThemeOverlayAlertDialog
            ).create()

        dialog.setCancelable(false)
        dialog.setView(dialogBinding.root)
        dialogBinding.ok.setOnClickListener {
            taskViewModel.deleteTask(taskList[currentPostion].id)
             dialog.dismiss()
        }
        dialogBinding.Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}