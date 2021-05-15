package com.omens.basiceventapp.service

import com.omens.basiceventapp.model.RetrievedItem
import retrofit2.Call
import retrofit2.http.GET

interface Service {
    @GET("getEvents")
    fun getEvents () : Call<MutableList<RetrievedItem>>

    @GET("getSchedule")
    fun getSchedule () : Call<MutableList<RetrievedItem>>
}