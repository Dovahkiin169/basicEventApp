package com.omens.basiceventapp.ui.playback

import android.graphics.Color
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.omens.basiceventapp.MainActivity
import com.omens.basiceventapp.R
import com.omens.basiceventapp.databinding.FragmentPlaybackBinding
import com.omens.basiceventapp.ui.event.EventViewModel


class PlaybackFragment : Fragment() {
    var cache: SimpleCache? = null
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar
    private var position: Long ?= null

    private val viewModel: EventViewModel by activityViewModels()

    private var _binding: FragmentPlaybackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaybackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(Color.parseColor("#000000"))
        val defaultLoadControl = DefaultLoadControl.Builder()
            .setPrioritizeTimeOverSizeThresholds(false).build()
        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext())
            .setLoadControl(defaultLoadControl)
            .build()
        playerView = binding.playerView
        progressBar = requireActivity().findViewById(R.id.mainProgressBar)
        playerView.player = simpleExoPlayer


        val dataSourceFactory =
            DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(),getString(
                R.string.app_name
            )))

        val cacheDataSourceFactory = CacheDataSource.Factory().apply {
            setCache(MainActivity.createSimpleCache())
            setUpstreamDataSourceFactory(dataSourceFactory)
        }

        val videoSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(viewModel.retrievedItem.videoUrl)))

        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                 if (state == PlaybackState.STATE_BUFFERING)
                     progressBar.visibility = View.VISIBLE
                 else
                     progressBar.visibility = View.GONE
            }
        })

        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoPlayer.setMediaSource(videoSource)
        simpleExoPlayer.prepare()
        if (savedInstanceState != null) {
            simpleExoPlayer.seekTo(savedInstanceState.getLong("STATE_POSITION"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        simpleExoPlayer.release()
        MainActivity.releaseSimpleCache()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        position = simpleExoPlayer.currentPosition
        if(position != null)
            outState.putLong("STATE_POSITION", position!!)
        else
            outState.putLong("STATE_POSITION", 0)
        super.onSaveInstanceState(outState)
    }
}