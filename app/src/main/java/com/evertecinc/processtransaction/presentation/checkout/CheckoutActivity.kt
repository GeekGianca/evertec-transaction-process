package com.evertecinc.processtransaction.presentation.checkout

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.databinding.ActivityCheckoutBinding
import com.evertecinc.processtransaction.presentation.states.TransactionProcessEventState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity(), CheckoutUIController {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCheckoutBinding

    private val viewModel: ProcessTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Only to save one time
        if (!SharedPreferenceManager.startState(this)) {
            viewModel.fakeInsertProductItem()
            SharedPreferenceManager.startState(this, true)
        }
        binding.cleanStackErrorBtn.setOnClickListener {
            viewModel.clearErrorStack()
            setAlertViewClean()
        }
        viewModel.setupChannel()
        viewModel.setStateEvent(TransactionProcessEventState.FetchInitialCheckoutEvent)
    }

    private fun setAlertViewClean() {
        binding.contentStackErrorView.hide()
        binding.contentStackView.hide()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_checkout)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDisplayProgress(state: Boolean) {
        if (state) {
            binding.contentStackView.show()
        } else {
            binding.contentStackView.hide()
        }
    }

    override fun onErrorDisplay(ex: ErrorState?) {
        binding.contentStackView.hide()
        binding.contentStackErrorView.show()
        binding.errorMessage.text = ex?.message
    }

}