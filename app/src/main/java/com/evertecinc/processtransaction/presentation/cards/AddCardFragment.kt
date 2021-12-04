package com.evertecinc.processtransaction.presentation.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.databinding.FragmentAddCardBinding
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.evertecinc.processtransaction.BuildConfig
import com.evertecinc.processtransaction.core.CheckoutUIController
import com.evertecinc.processtransaction.core.Util
import com.evertecinc.processtransaction.core.asList
import com.evertecinc.processtransaction.core.buildInfoCard
import com.evertecinc.processtransaction.domain.model.request.*
import com.evertecinc.processtransaction.presentation.checkout.CheckoutActivity
import com.evertecinc.processtransaction.presentation.checkout.ProcessTransactionViewModel
import com.evertecinc.processtransaction.presentation.states.TransactionProcessEventState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class AddCardFragment : Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProcessTransactionViewModel by activityViewModels()
    private lateinit var auth: AuthModel
    private lateinit var payer: PayerModel
    private lateinit var instrument: Instrument
    private lateinit var tokenize: TokenizeReqModel

    private lateinit var uIController: CheckoutUIController

    private val listenerText = object : TextWatcher {
        private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
        private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
        private val DIVIDER_MODULO =
            5 // means divider position is every 5th symbol beginning with 1
        private val DIVIDER_POSITION =
            DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
        private val DIVIDER = ' ' // Divider object

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(
                        0,
                        s.toString().length,
                        buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER
                        )
                    )
                    binding.typeCreditCard.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            Util.getImageCreditCard(s.toString().trim().replace(" ", ""))
                        )
                    )
                }
            }
        }

        private fun isInputCorrect(
            s: Editable,
            totalSymbols: Int,
            dividerModulo: Int,
            divider: Char
        ): Boolean {
            var isCorrect = s.length <= totalSymbols // check size of entered string
            for (i in s.indices) { // check that every element is right
                isCorrect = if (i > 0 && (i + 1) % dividerModulo == 0) {
                    isCorrect and (divider == s[i])
                } else {
                    isCorrect and Character.isDigit(s[i])
                }
            }
            return isCorrect
        }

        private fun buildCorrectString(
            digits: CharArray,
            dividerPosition: Int,
            divider: Char
        ): String {
            val formatted = StringBuilder()
            for (i in digits.indices) {
                if (digits[i].code != 0) {
                    formatted.append(digits[i])
                    if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                        formatted.append(divider)
                    }
                }
            }
            return formatted.toString()
        }

        private fun getDigitArray(s: Editable, size: Int): CharArray {
            val digits = CharArray(size)
            var index = 0
            var i = 0
            while (i < s.length && index < size) {
                val current = s[i]
                if (Character.isDigit(current)) {
                    digits[index] = current
                    index++
                }
                i++
            }
            return digits
        }

    }

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
        _binding = FragmentAddCardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservables()
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.cardNumber.addTextChangedListener(listenerText)
        binding.expirationDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 1 && start + added == 2 && p0?.contains('/') == false) {
                    val month = p0.toString().toInt()
                    if (month <= 12) {
                        binding.expirationDate.setText(getString(R.string.expiration_date_args, p0))
                        binding.expirationDate.setSelection(5)
                        binding.layoutExpirationNumber.error = null
                    } else {
                        binding.layoutExpirationNumber.error = getString(R.string.error_month_valid)
                        binding.expirationDate.setSelection(1)
                        binding.expirationDate.setText("")
                    }
                } else if (start == 3 && start - removed == 2 && p0?.contains('/') == true) {
                    binding.expirationDate.setText(p0.toString().replace("/", ""))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.savePaymentMethod.setOnClickListener {
            validateInputs()
            auth = AuthModel(BuildConfig.AUTH, Util.nonceBase64, Util.seed, Util.tranKeyBase64)
            payer = PayerModel(
                "",
                "",
                binding.email.text.toString(),
                "",
                binding.name.text.toString(),
                binding.surname.text.toString()
            )
            instrument = Instrument(
                card = CardModel(
                    binding.cvv.text.toString(),
                    binding.expirationDate.text.toString().replace(" ", ""),
                    0,
                    binding.cardNumber.text.toString().replace(" ", "")
                )
            )
            tokenize = TokenizeReqModel(auth = auth, instrument = instrument, payer = payer)
            uIController.onDisplayProgress(true)
            viewModel.setStateEvent(
                TransactionProcessEventState.CreateTokenizedCreditCardEvent(
                    tokenize
                )
            )
        }
        viewModel.clearErrorStack()
    }

    private fun subscribeObservables() {
        viewModel.viewSate.observe(viewLifecycleOwner) { viewState ->
            if (viewState != null) {
                viewState.createCardViews.card?.let {
                    uIController.onDisplayProgress(false)
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            Log.d(this::class.java.name, "Error: $it")
            uIController.onErrorDisplay(it)
        }
    }

    private fun validateInputs() {
        if (binding.name.text?.toString()?.isEmpty() == true) {
            binding.layoutName.error = getString(R.string.error_name)
            return
        } else {
            binding.layoutName.error = null
        }
        if (binding.surname.text?.toString()?.isEmpty() == true) {
            binding.layoutSurName.error = getString(R.string.error_surname)
            return
        } else {
            binding.layoutSurName.error = null
        }
        if (binding.email.text?.toString()?.isEmpty() == true) {
            binding.layoutEmail.error = getString(R.string.error_email)
            return
        } else {
            binding.layoutEmail.error = null
        }
        if (binding.cardNumber.text?.toString()?.isEmpty() == true) {
            binding.layoutCardNumber.error = getString(R.string.error_credit_number)
            return
        } else {
            binding.layoutCardNumber.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCreateCardViewState()
        uIController.onDisplayProgress(false)
    }

}