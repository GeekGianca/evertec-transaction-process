package com.evertecinc.processtransaction.presentation.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.databinding.FragmentResumeTransactionBinding
import com.evertecinc.processtransaction.domain.model.ResumeTransactionModel
import com.evertecinc.processtransaction.presentation.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResumeTransactionFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentResumeTransactionBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "ResumeTransactionFragme"
    }

    private var argObject: ResumeTransactionModel? = null
    private var argFrom: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argObject = arguments?.getParcelable(Constants.TXT_DETAIL)
            argFrom = arguments?.getString(Constants.FROM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            if (argFrom == getString(R.string.from_checkout)) {
                requireContext().startActivity(Intent(requireContext(), MainActivity::class.java))
            } else {
                findNavController().popBackStack()
            }
        }

        setViewData()
    }

    private fun setViewData() {
        argObject?.let {
            binding.detailTransaction.text = getString(
                R.string.detail_arg,
                it.quantity,
                getString(R.string.cost_cop, Commons.formatDecimal.format(it.totalAmount))
            )
            binding.paymentDetailCreditCard.text =
                getString(R.string.franchise_args, it.franchise, it.lastDigits)
            binding.dateTransaction.text = it.date.asFormatUser()
            binding.status.text = it.status.capitalize()
            val color = when (it.status) {
                Constants.APPROVED -> {
                    ContextCompat.getColor(requireContext(), R.color.success)
                }
                Constants.REJECTED -> {
                    ContextCompat.getColor(requireContext(), R.color.success)
                }
                else -> {
                    ContextCompat.getColor(requireContext(), R.color.success)
                }
            }
            binding.status.setTextColor(color)
            binding.payerName.text = it.name.capitalize()
            binding.address.text = it.shipmentAddress
            binding.detail.text = getString(R.string.args_info, it.quantity, it.detail)
            binding.totalPurchase.text =
                getString(R.string.cost_cop, Commons.formatDecimal.format(it.totalAmount))
            binding.transactionCard.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    Util.getImageFromFranchise(it.franchise)
                )
            )
        }
    }
}