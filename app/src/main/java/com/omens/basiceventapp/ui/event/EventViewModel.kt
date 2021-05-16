package com.omens.basiceventapp.ui.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omens.basiceventapp.model.RetrievedItem

class EventViewModel : ViewModel() {
    lateinit var retrievedItem: RetrievedItem
    var mutableLiveData: MutableLiveData<MutableList<RetrievedItem>> = MutableLiveData()
}