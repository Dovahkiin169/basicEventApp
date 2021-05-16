package com.omens.basiceventapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.omens.basiceventapp.R
import com.omens.basiceventapp.model.RetrievedItem
import com.omens.basiceventapp.ui.event.EventViewModel
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class RecyclerViewAdapter(
    private val mListener: OnFragmentInteractionListener,
    private val viewModel: ViewModel
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var isClickable = true
    private val mOnClickListener: View.OnClickListener
    private var values: List<RetrievedItem> = listOf()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as RetrievedItem
            viewModel as EventViewModel
            viewModel.retrievedItem = item
            if(isClickable)
                mListener.onListItemSelect()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
       // detourItemRoomFactory = UsersItemRoomFactory(context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.updateView(item)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val titleTextView: TextView = mView.findViewById(R.id.titleTextView)
        private val subtitleTextView: TextView = mView.findViewById(R.id.subtitleTextView)
        private val dateTextView: TextView = mView.findViewById(R.id.dateTextView)
        private var imagePreview: ImageView = mView.findViewById(R.id.imagePreview)

        override fun toString(): String {
            return "ViewHolder(mView=$mView, title=$titleTextView, subtitle=$subtitleTextView, date=$dateTextView"
        }

        fun updateView(item: RetrievedItem){
            Picasso.get().load(item.imageUrl).into(imagePreview)
            titleTextView.text = item.title
            subtitleTextView.text = item.subtitle
            dateTextView.text = DateUtil.similarToMockDate(item.date!!)
        }
    }


    fun reloadList(items: List<RetrievedItem>) {
        values = items
        notifyDataSetChanged()
    }

    fun returnList():MutableList<RetrievedItem> {
        return values.toMutableList()
    }


    object DateUtil {
        private fun dayCheck(date: Date, days: Int): Date {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DATE, days)
            return cal.time
        }
        fun similarToMockDate(dateFromItem: String): String {
            val format: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val calFromItem = Calendar.getInstance()
            calFromItem.time = format.parse(dateFromItem)
            val calCurrent = Calendar.getInstance()
            calCurrent.time = Date()

            val toDate: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
            val toHoursAndMinutes: DateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

            val currentDate = toDate.format(calCurrent.time)

            val date = toDate.format(calFromItem.time)
            val hoursAndMinutes = toHoursAndMinutes.format(calFromItem.time)

            return when (date) {
                currentDate -> "Today, $hoursAndMinutes"
                toDate.format(dayCheck(calCurrent.time,-1)) -> "Yesterday, $hoursAndMinutes"
                toDate.format(dayCheck(calCurrent.time,+1)) -> "Tomorrow, $hoursAndMinutes"
                toDate.format(dayCheck(calCurrent.time,+2)) -> "In two days, $hoursAndMinutes"
                else -> "$date, $hoursAndMinutes"
            }
        }
    }
}