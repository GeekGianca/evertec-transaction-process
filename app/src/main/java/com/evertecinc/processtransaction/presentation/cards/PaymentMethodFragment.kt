package com.evertecinc.processtransaction.presentation.cards

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.databinding.FragmentSecondBinding
import com.evertecinc.processtransaction.presentation.adapter.AdapterPaymentMethod
import com.evertecinc.processtransaction.presentation.checkout.CheckoutActivity
import com.evertecinc.processtransaction.presentation.checkout.ProcessTransactionViewModel
import com.evertecinc.processtransaction.presentation.states.TransactionProcessEventState

class PaymentMethodFragment : Fragment(), AdapterPaymentMethod.PaymentMethodInteraction {
    companion object {
        private const val TAG = "PaymentMethodFragment"
    }

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val creditCards = mutableListOf<TokenCreditCardEntity>()
    private lateinit var adapterCards: AdapterPaymentMethod
    private var cardSelected: Long = 0
    private val viewModel: ProcessTransactionViewModel by activityViewModels()

    private lateinit var uIController: CheckoutUIController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (activity is CheckoutActivity) {
                try {
                    uIController = context as CheckoutActivity
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
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservables()
        viewModel.setStateEvent(TransactionProcessEventState.ListTokenizedCreditCardsEvent)
    }

    private fun subscribeObservables() {
        viewModel.viewSate.observe(viewLifecycleOwner) { viewState ->
            if (viewState != null) {
                uIController.onDisplayProgress(false)
                viewState.cardsViews.cards?.let {
                    creditCards.clear()
                    creditCards.add(0, Util.empty())
                    creditCards.addAll(it)
                    adapterCards = AdapterPaymentMethod(creditCards, this)
                    binding.recyclerView.adapter = adapterCards
                }

                viewState.attachPayment.status?.let {
                    Log.d(TAG, "Attach payment: $it")
                    if (it) {
                        findNavController().popBackStack()
                    }
                }
            }
        }

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
            uIController.onDisplayProgress(it)
        }

    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.changePaymentMethod.setOnClickListener {
            viewModel.setStateEvent(
                TransactionProcessEventState.ChangePaymentMethodEvent(
                    cardSelected
                )
            )
        }
        initRecycler()
    }

    private fun initRecycler() {
        //Only to show add card button
        creditCards.add(0, Util.empty())
        adapterCards = AdapterPaymentMethod(creditCards, this)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = adapterCards
        }
    }

    override fun onAddNewCreditCard() {
        findNavController().navigate(R.id.action_paymentMethodFragment_to_addCardFragment)
    }

    override fun onSelectedCreditCard(item: TokenCreditCardEntity, index: Int, lastIndex: Int) {
        cardSelected = item.id
        adapterCards.notifyItemChanged(index)
        adapterCards.notifyItemChanged(lastIndex)
        binding.layoutCard.contentCard.background = ContextCompat.getDrawable(
            requireContext(),
            Util.getBackgroundFromFranchise(item.franchise)
        )
        binding.layoutCard.creditCardName.text = item.franchiseName
        binding.layoutCard.creditCardNameUser.text = item.name.capitalize()
        binding.layoutCard.validDateCreditCard.text = item.validUntil.validThuFormat()
        binding.layoutCard.creditCardNumber.text = item.digits.formatCreditCard()
        binding.layoutCard.creditCardIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                Util.getImageFromFranchise(item.franchise)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.removeAddCardViewState()
        uIController.onDisplayProgress(false)
    }
}