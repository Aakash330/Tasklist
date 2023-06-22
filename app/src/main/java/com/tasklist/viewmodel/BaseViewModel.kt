package com.tasklist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel :ViewModel() {
      var onError=MutableLiveData<String>()
      var onSuccess=MutableLiveData<Any>()
}