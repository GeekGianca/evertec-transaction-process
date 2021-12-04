package com.evertecinc.processtransaction.presentation.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.core.Constants.FROM
import com.evertecinc.processtransaction.core.Constants.TXT_DETAIL
import com.evertecinc.processtransaction.databinding.FragmentFirstBinding
import com.evertecinc.processtransaction.presentation.main.MainActivity
import com.evertecinc.processtransaction.presentation.states.TransactionProcessEventState
import com.evertecinc.processtransaction.presentation.states.TransactionViewStates
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    companion object {
        private const val TAG = "CheckoutFragment"
    }

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var uIController: CheckoutUIController

    //Fake product detail
    private var priceUnit = 0.0
    private var priceShipment = 0.0
    private var totalPrice = 0.0
    private var installments = 0
    private var canExecuteTransaction = false
    private var isInstallmentsAttached = false
    private var shipment = ""

    private val viewModel: ProcessTransactionViewModel by activityViewModels()

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
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservables()
    }

    private fun subscribeObservables() {
        viewModel.viewSate.observe(viewLifecycleOwner) { viewState ->
            if (viewState != null) {
                viewState.itemView.product?.let {
                    priceUnit = it.productEntity.unitPrice
                    binding.productItemName.text = it.productEntity.productName
                    binding.productItemPrice.text = getString(
                        R.string.price_cop_value,
                        Commons.formatDecimal.format(it.productEntity.unitPrice)
                    )
                    binding.infoDetailItem.text = it.productEntity.description
                    binding.quantityItem.setText("${it.productEntity.quantity}")

                    if (it.tokenCreditCard == null) {
                        binding.typeCardCredit.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_error_outline
                            )
                        )
                    } else {
                        canExecuteTransaction = true
                        binding.typeCardCredit.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                Util.getImageFromFranchise(it.tokenCreditCard.franchise)
                            )
                        )
                        if (!isInstallmentsAttached) {
                            viewModel.setStateEvent(
                                TransactionProcessEventState.RequestCardInformationEvent(
                                    it.buildInfoCard()
                                )
                            )
                        }
                    }
                }

                viewState.infoCardView.info?.let {
                    if (it.installments != null) {
                        isInstallmentsAttached = true
                        canExecuteTransaction = true
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            it.installments!!.asList()
                        )
                        binding.installments.setAdapter(adapter)
                    } else {
                        canExecuteTransaction = false
                    }
                }

                viewState.transactionViews.transaction?.let {
                    uIController.onDisplayProgress(false)
                    val args = Bundle()
                    args.putParcelable(TXT_DETAIL, it.toResumeTransactionModel())
                    args.putString(FROM, getString(R.string.from_checkout))
                    findNavController().navigate(
                        R.id.resumeTransactionFragment,
                        args
                    )
                }
            }
        }

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
            uIController.onDisplayProgress(it)
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            Log.e(TAG, "ErrorState: $it")
            uIController.onErrorDisplay(it)
        }

    }

    private fun initView() {
        binding.quantityItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val quantity = p0.toString().toInt()
                setTotalByQuantity(quantity)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    requireContext().startActivity(
                        Intent(requireContext(), MainActivity::class.java)
                    )
                    true
                }
                else -> false
            }
        }
        binding.selectAllCb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.itemCardCheckout.show()
                binding.divider.show()
            } else {
                canExecuteTransaction = isChecked
                binding.itemCardCheckout.hide()
                binding.divider.hide()
            }
        }
        binding.paymentMethodBtn.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentMethodFragment)
        }
        binding.removeBtn.setOnClickListener {
            binding.quantityItem.setText(getString(R.string.empty_value))
            Snackbar.make(binding.root, getString(R.string.remove_info), Snackbar.LENGTH_LONG)
                .show()
        }
        binding.plusBtn.setOnClickListener {
            viewModel.setStateEvent(TransactionProcessEventState.UpdateProductItemEvent(0))
        }
        binding.minusBtn.setOnClickListener {
            viewModel.setStateEvent(TransactionProcessEventState.UpdateProductItemEvent(1))
        }
        binding.removeAll.setOnClickListener {
            binding.quantityItem.setText(getString(R.string.empty_value))
            Snackbar.make(binding.root, getString(R.string.fake_remove_all), Snackbar.LENGTH_LONG)
                .show()
        }
        binding.shipmentBtn.setOnClickListener {
            setFakeShipment()
        }
        binding.buttonPay.setOnClickListener {
            if (binding.installments.text.toString().isNotEmpty() && canExecuteTransaction) {
                //Make transaction
                installments = binding.installments.text.toString().toInt()
                val process =
                    (viewModel.viewSate.value?.itemView as TransactionViewStates.ProductItemViews).product
                val transaction = process!!.toTransaction(totalPrice, installments)
                Log.d(TAG, "Transaction: $transaction")
                viewModel.setStateEvent(
                    TransactionProcessEventState.ProcessTransactionEvent(
                        transaction,
                        process.productEntity,
                        shipment
                    )
                )
            } else {
                requireContext().showToast(getString(R.string.cant_execute_transaction))
            }
        }
        setFakeShipment()
    }

    private fun setFakeShipment() {
        val randomAddress = listOf(
            "1530 Eva Pearl Street, Florida",
            "2708 Woodside Circle, Florida",
            "3493 Single Street, Massachusetts"
        )
        val randomIdxAddress = (0..2).random()
        val random = (20000..112000).random()
        priceShipment = random.toDouble()
        shipment = randomAddress[randomIdxAddress]
        binding.locationTx.text =
            getString(R.string.location_args, shipment)
        binding.costTx.text =
            getString(R.string.cost_cop, Commons.formatDecimal.format(priceShipment))
        val quantity = if (binding.quantityItem.text.toString().isEmpty()) {
            0
        } else {
            binding.quantityItem.text.toString().toInt()
        }
        setTotalByQuantity(quantity)
    }

    private fun setTotalByQuantity(quantity: Int) {
        totalPrice = (quantity * priceUnit) + priceShipment
        binding.buttonPay.text =
            getString(R.string.pay_now, Commons.formatDecimal.format(totalPrice))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearAllViewStates()
    }
}