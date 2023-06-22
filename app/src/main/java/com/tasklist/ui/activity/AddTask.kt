package com.tasklist.ui.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tasklist.MyApplication
import com.tasklist.R
import com.tasklist.databinding.ActivityAddTaskBinding
import com.tasklist.model.TaskTable
import com.tasklist.util.AppConstent
import com.tasklist.util.convertDatesFromat
import com.tasklist.util.toToast
import com.tasklist.viewmodel.TaskViewModel
import com.tasklist.viewmodel.TaskViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class AddTask : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddTaskBinding
    private var timestamp: Long = System.currentTimeMillis()
    private var formated_time: String? = null
    private var formatDate: String = ""

    @Inject
    lateinit var taskViewModelFactory: TaskViewModelFactory
    lateinit var taskViewModel: TaskViewModel
    lateinit var taskTable: TaskTable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as MyApplication).applicationComponent.inject(this)
        taskViewModel = ViewModelProvider(this, taskViewModelFactory)[TaskViewModel::class.java]
        initClick()
        initObserver()


    }

    private fun initObserver() {
        taskViewModel.onError.observe(this) {
            this.toToast(it)
        }
        taskViewModel.onSuccess.observe(this) {
            val intent = Intent()
            val data = Gson().toJson(taskTable)
            intent.putExtra(AppConstent.TASK_DATA, data)
            setResult(RESULT_OK, intent)
            finish()
        }

        taskViewModel.onError.observe(this) {
            this.toToast(it.toString())
        }
    }

    private fun initClick() {
        binding.btnadd.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.ettasktime.setOnClickListener(this)
    }


    override fun onClick(view: View?) {

        when (view?.id) {

            R.id.btnCancel -> onBackPressed()
            R.id.ivBack -> onBackPressed()
            R.id.ettasktime -> taskTime()
            R.id.btnadd -> addDatainDB()
        }


    }

    /**
     * instance variable to handle the click event of time
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            try {

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                //store the time stamp according to time and date
                val formatting_date =
                    "$year-$month-$day $hourOfDay:$minute:00"
                formatDate = LocalDate.of(year, month, day).toString()
                //  formatDate= "$year-$month-$day"
                val convertedTime=   formatting_date.convertDatesFromat(
                    "yyyy-MM-dd HH:mm:ss",
                    "H:mm aa",
                )
                val ampm = convertedTime.split(" ").get(1).toUpperCase()
                val formatingStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                //  formatingStamp.timeZone= TimeZone.getTimeZone("IST")
                val parsingDate = formatingStamp.parse(formatting_date)
                timestamp = parsingDate.time

                val sdf = SimpleDateFormat("H:mm")
                val dateObj: Date = sdf.parse("$hourOfDay:$minute")
                formated_time = SimpleDateFormat("KK:mm").format(dateObj)
                if (hourOfDay == 0) {
                    binding.ettasktime.setText("12:$minute")
                } else {
                    binding.ettasktime.setText("$formated_time")
                }
                binding.ettaskam.setText(ampm)


            } catch (e: ParseException) {
                e.printStackTrace()
            }


        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun taskTime() {
        val timePicker: TimePickerDialog = TimePickerDialog(
            this@AddTask,
            timePickerDialogListener,
            12,
            10,
            false
        )

        timePicker.show()
    }


    private fun addDatainDB() {

        if (checkValidation()) {

            val time = binding.ettasktime.text.toString() + " ${binding.ettaskam.text.toString()}"
            taskTable = TaskTable(
                System.currentTimeMillis(),
                binding.ettasname.text.toString(),
                timestamp,
                time,
                formatDate,
                AppConstent.ASSIGN
            )
            taskViewModel.addTask(taskTable)
        }


    }

    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(binding.ettasname.text.toString())) {
            this.toToast(getString(R.string.error_task_name))
            return false
        } else if (TextUtils.isEmpty(binding.ettasktime.text.toString())) {
            this.toToast(getString(R.string.error_task_time))
            return false
        }

        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }
}