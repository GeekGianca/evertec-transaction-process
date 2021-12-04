package com.evertecinc.processtransaction.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.Constants
import com.evertecinc.processtransaction.core.MainUIController
import com.evertecinc.processtransaction.core.toResumeTransactionModel
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.databinding.FragmentDashboardBinding
import com.evertecinc.processtransaction.presentation.adapter.AdapterTransaction
import com.evertecinc.processtransaction.presentation.states.MainEventState

class DashboardFragment : Fragment(), AdapterTransaction.TransactionInteraction {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapterTransaction: AdapterTransaction
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
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservables()
        viewModel.setStateEvent(MainEventState.TransactionListEvent)
    }

    private fun subscribeObservables() {
        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
            uIController.onDisplayLoading(it)
        }
        viewModel.errorState.observe(viewLifecycleOwner) {
            uIController.onErrorDisplay(it!!)
        }

        viewModel.viewSate.observe(viewLifecycleOwner) { viewState ->
            if (viewState != null) {
                viewState.transactions.transactions?.let {
                    adapterTransaction = AdapterTransaction(it, this)
                    binding.transactionList.adapter = adapterTransaction
                }
            }
        }
    }

    private fun initView() {
        binding.transactionList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelectTransaction(item: TransactionWithDetailsRelation) {
        val args = Bundle()
        args.putParcelable(Constants.TXT_DETAIL, item.toResumeTransactionModel())
        findNavController().navigate(R.id.resumeTransactionFragment, args)
    }
}