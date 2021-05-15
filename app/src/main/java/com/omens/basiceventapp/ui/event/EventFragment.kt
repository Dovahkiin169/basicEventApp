package com.omens.basiceventapp.ui.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omens.basiceventapp.OnFragmentInteractionListener
import com.omens.basiceventapp.databinding.FragmentEventsBinding
import com.omens.basiceventapp.model.EventItem
import com.omens.basiceventapp.service.EventsService
import com.omens.basiceventapp.service.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EventFragment : Fragment() {

    private lateinit var adapter: EventRecyclerViewAdapter
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var eventViewModel: EventViewModel
    private var _binding: FragmentEventsBinding? = null
    private var eventRecyclerView: RecyclerView? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = EventRecyclerViewAdapter(listener!!)
        eventRecyclerView = binding.eventsList
        with(eventRecyclerView as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EventFragment.adapter
        }

        loadEvents()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun loadEvents() {
        val destinationService = ServiceBuilder.buildService(EventsService::class.java)
        val requestCall = destinationService.getEvents()
        requestCall.enqueue(object : Callback<MutableList<EventItem>> {
            override fun onResponse(call: Call<MutableList<EventItem>>, response: Response<MutableList<EventItem>>) {
                if (response.isSuccessful){
                    val eventsList  = response.body()!!
                    val format: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                    val sortedList = eventsList.sortedBy { format.parse(it.date!!) }

                    adapter.reloadList(sortedList)
                }else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<MutableList<EventItem>>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}