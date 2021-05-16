package com.omens.basiceventapp.ui.schedule

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omens.basiceventapp.R
import com.omens.basiceventapp.databinding.FragmentScheduleBinding
import com.omens.basiceventapp.utils.OnFragmentInteractionListener
import com.omens.basiceventapp.utils.RecyclerViewAdapter
import com.omens.basiceventapp.utils.loadData
import java.util.*

class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private var _binding: FragmentScheduleBinding? = null

    private lateinit var adapter: RecyclerViewAdapter
    private var listener: OnFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: ScheduleViewModel by activityViewModels()

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var delay = 30000

    private lateinit var progressBar: ProgressBar

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scheduleViewModel =
            ViewModelProvider(this).get(ScheduleViewModel::class.java)

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root


        adapter = RecyclerViewAdapter(listener!!,viewModel)
        adapter.isClickable = false
        recyclerView = binding.scheduleList
        with(recyclerView as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ScheduleFragment.adapter
        }

        progressBar = requireActivity().findViewById(R.id.mainProgressBar)

        loadData(false,requireContext(),adapter,progressBar)



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

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            loadData(false,requireContext(),adapter,progressBar)
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
    }
}