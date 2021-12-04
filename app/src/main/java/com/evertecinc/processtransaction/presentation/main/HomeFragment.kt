package com.evertecinc.processtransaction.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evertecinc.processtransaction.core.MainUIController
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.databinding.FragmentHomeBinding
import com.evertecinc.processtransaction.presentation.adapter.AdapterPaymentMethod
import com.evertecinc.processtransaction.presentation.adapter.AdapterTokenizedCard
import com.evertecinc.processtransaction.presentation.checkout.CheckoutActivity
import com.evertecinc.processtransaction.presentation.states.MainEventState
import com.evertecinc.processtransaction.presentation.states.MainViewStates

class HomeFragment : Fragment(), AdapterPaymentMethod.PaymentMethodInteraction {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapterCards: AdapterTokenizedCard
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var uIController: MainUIController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (activity is MainActivity) {
                try {
                    uIController = context as MainActivity
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservables()
        viewModel.setStateEvent(MainEventState.CardListEvent)
    }

    private fun initView() {
        binding.listCards.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun subscribeObservables() {
        viewModel.viewSate.observe(viewLifecycleOwner) { viewState ->
            if (viewState != null) {
                viewState.cards.list?.let {
                    adapterCards = AdapterTokenizedCard(it)
                    binding.listCards.adapter = adapterCards
                }
            }
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            uIController.onDisplayLoading(false)
            uIController.onErrorDisplay(it!!)
        }

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
            uIController.onDisplayLoading(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAddNewCreditCard() {

    }
}