package com.tasklist.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tasklist.R
import com.tasklist.db.TaskDb
import com.tasklist.model.TaskTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskViewModel @Inject constructor(private val taskDb: TaskDb) : BaseViewModel() {
     val allTaskListData = MutableLiveData<ArrayList<TaskTable>>()
     val deleteObserver=MutableLiveData<String>()
     val updateObserver=MutableLiveData<String>()




    fun addTask(taskTable: TaskTable) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                 taskDb.getDao().addTask(taskTable)
                 onSuccess.postValue(R.string.task_added)
            } catch (e: Exception) {
                onError.postValue(e.message)
            }

        }
    }

    fun getAllTask() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = taskDb.getDao().getAllTask()
                if (data.isNullOrEmpty())
                    allTaskListData.postValue(ArrayList())
                else
                    allTaskListData.postValue( data as ArrayList<TaskTable>)
            } catch (e: Exception) {
                onError.postValue( e.message)
            }
        }
    }



    //apply the filter by according to date
    fun getByFilter(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = taskDb.getDao().filterData(date)
                if (data.isNullOrEmpty())
                    allTaskListData.postValue(ArrayList())
                else
                    allTaskListData.postValue( data as ArrayList<TaskTable>)
            } catch (e: Exception) {
                onError.postValue( e.message)
            }
        }
    }

    fun getByFilter(timeMili:Long)
    {

    }


    fun updateStatus(status:String,id: Long)
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                taskDb.getDao().updateStatus(status,id)
                updateObserver.postValue("")
              //  onSuccess.postValue(R.string.task_removed)
            } catch (e: Exception) {
                onError.postValue(e.message)
            }
        }
    }
    fun deleteTask(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                taskDb.getDao().deleteTask(id)
                deleteObserver.postValue("")
                onSuccess.postValue(R.string.task_removed)
            } catch (e: Exception) {
                onError.postValue(e.message)
            }
        }
    }


}