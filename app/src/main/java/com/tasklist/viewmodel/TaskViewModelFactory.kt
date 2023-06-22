package com.tasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasklist.db.TaskDb
import javax.inject.Inject

class TaskViewModelFactory @Inject constructor(val taskDb: TaskDb): ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(taskDb ) as T

    }
}