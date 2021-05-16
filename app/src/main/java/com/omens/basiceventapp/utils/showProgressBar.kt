package com.omens.basiceventapp.utils

import android.view.View
import android.widget.ProgressBar


fun showProgressBar(progressBar: ProgressBar, value: Boolean){
    if(value)
        progressBar.visibility = View.VISIBLE
    else
        progressBar.visibility = View.GONE
}