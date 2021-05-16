package com.omens.basiceventapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.omens.basiceventapp.databinding.ActivityMainBinding
import com.omens.basiceventapp.utils.OnFragmentInteractionListener
import java.io.File

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.eventsFragments, R.id.scheduleFragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.playbackFragment) {
                (this@MainActivity as AppCompatActivity?)!!.supportActionBar!!.hide()
                navView.visibility = View.GONE
            } else {
                (this@MainActivity as AppCompatActivity?)!!.supportActionBar!!.show()
                navView.visibility = View.VISIBLE
            }
        }

        progressBar = binding.mainProgressBar

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onListItemSelect() {
        navController.navigate(R.id.action_eventsFragment_to_playbackFragment)
    }

    companion object {
        private lateinit var appContext : Context
        private var simpleCache : SimpleCache? = null
        private const val cacheSize = 2L * 1024L * 1024L * 1024L // 2GB

        private fun getAppContext() : Context {
            return appContext
        }

        fun createSimpleCache() : SimpleCache {
            simpleCache?.release()
            val databaseProvider: DatabaseProvider = ExoDatabaseProvider(getAppContext())
            simpleCache = SimpleCache(File(getAppContext().cacheDir, "cache"), LeastRecentlyUsedCacheEvictor(cacheSize), databaseProvider)
            return simpleCache!!
        }

        fun releaseSimpleCache() {
            simpleCache?.release()
        }
    }

}