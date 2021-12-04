package com.evertecinc.processtransaction.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainUIController {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHost.navController
        binding.navView.setupWithNavController(navController)
        viewModel.setupChannel()
    }

    override fun onDisplayLoading(state: Boolean) {
        if (state)
            binding.contentStackView.show()
        else
            binding.contentStackView.hide()
    }

    override fun onErrorDisplay(ex: ErrorState) {
        showToast(ex.message)
    }
}