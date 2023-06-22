package com.tasklist.adapter

interface RvListner {
    fun changeStatus(pos:Int,status:String)
    fun delete(pos:Int)

}