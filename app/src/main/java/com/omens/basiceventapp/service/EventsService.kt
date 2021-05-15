package com.omens.basiceventapp.service

import com.omens.basiceventapp.model.EventItem
import retrofit2.Call
import retrofit2.http.GET

interface EventsService {
    @GET("getEvents")
    fun getEvents () : Call<MutableList<EventItem>>
}