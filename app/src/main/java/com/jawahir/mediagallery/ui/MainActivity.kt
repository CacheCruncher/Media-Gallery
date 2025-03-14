package com.jawahir.mediagallery.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbarLayout.toolbarBack.setOnClickListener {
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            } else {
                finish() // Close the app
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.albumFragment -> {
                    binding.toolbarLayout.toolbarTitle.text = getString(R.string.album)
                }
                R.id.albumDetailFragment -> {
                    binding.toolbarLayout.toolbarTitle.text = getString(R.string.album_details)
                }
                R.id.imageViewerFragment -> {
                    binding.toolbarLayout.toolbarTitle.text = getString(R.string.photo)
                }
                else -> {
                    binding.toolbarLayout.toolbarTitle.text = getString(R.string.video)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}