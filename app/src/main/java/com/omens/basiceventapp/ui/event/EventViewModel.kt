package com.omens.basiceventapp.ui.event

import androidx.lifecycle.ViewModel
import com.omens.basiceventapp.model.RetrievedItem

class EventViewModel : ViewModel() {
    lateinit var retrievedItem: RetrievedItem
}