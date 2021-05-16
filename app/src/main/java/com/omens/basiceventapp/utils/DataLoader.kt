package com.omens.basiceventapp.utils

import android.content.Context
import android.widget.ProgressBar
import android.widget.Toast
import com.omens.basiceventapp.model.RetrievedItem
import com.omens.basiceventapp.service.Service
import com.omens.basiceventapp.service.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

    fun loadData(isEvents: Boolean, context: Context, adapter: RecyclerViewAdapter, progressBar: ProgressBar) {
        val service = ServiceBuilder.buildService(Service::class.java)

        val requestCall = if(isEvents)
            service.getEvents()
        else
            service.getSchedule()

        showProgressBar(progressBar,true)
        requestCall.enqueue(object : Callback<MutableList<RetrievedItem>> {
            override fun onResponse(call: Call<MutableList<RetrievedItem>>, response: Response<MutableList<RetrievedItem>>) {
                if (response.isSuccessful){
                    val eventsList  = response.body()!!
                    val format: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                    val sortedList = eventsList.sortedBy { format.parse(it.date!!) }
                    adapter.reloadList(sortedList)
                }else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
                showProgressBar(progressBar,false)
            }
            override fun onFailure(call: Call<MutableList<RetrievedItem>>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
                showProgressBar(progressBar,true)
            }
        })
    }