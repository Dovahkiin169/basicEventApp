package com.omens.basiceventapp.ui.event

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omens.basiceventapp.R
import com.omens.basiceventapp.databinding.FragmentEventsBinding
import com.omens.basiceventapp.utils.OnFragmentInteractionListener
import com.omens.basiceventapp.utils.RecyclerViewAdapter
import com.omens.basiceventapp.utils.loadData
import java.util.*

class EventFragment : Fragment() {

    private lateinit var adapter: RecyclerViewAdapter
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var eventViewModel: EventViewModel
    private var _binding: FragmentEventsBinding? = null
    private var eventRecyclerView: RecyclerView? = null
    private val viewModel: EventViewModel by activityViewModels()

    private lateinit var progressBar: ProgressBar

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = RecyclerViewAdapter(listener!!,viewModel)
        adapter.isClickable = true
        eventRecyclerView = binding.eventsList
        with(eventRecyclerView as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EventFragment.adapter
        }

        progressBar = requireActivity().findViewById(R.id.mainProgressBar)
        loadData(true,requireContext(),adapter,progressBar)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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