package com.evertecinc.processtransaction.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.Util
import com.evertecinc.processtransaction.core.hide
import com.evertecinc.processtransaction.core.show
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.databinding.CreditCardItemLayoutBinding

class AdapterPaymentMethod(
    private val data: List<TokenCreditCardEntity>,
    private val interaction: PaymentMethodInteraction
) :
    RecyclerView.Adapter<AdapterPaymentMethod.PaymentMethodViewHolder>() {
    private var selectedIndexCard = -1
    private var _context: Context? = null
    private val context: Context
        get() = _context!!

    inner class PaymentMethodViewHolder(private val binding: CreditCardItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TokenCreditCardEntity, position: Int) {
            binding.apply {
                if (item.id == 0L) {
                    this.addNewCardText.show()
                    this.imageCreditCard.hide()
                    this.root.setOnClickListener {
                        interaction.onAddNewCreditCard()
                    }
                    this.backgroundCreditCard.hide()
                } else {
                    this.imageCreditCard.show()
                    this.addNewCardText.hide()
                    this.imageCreditCard.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            Util.getImageFromFranchise(item.franchise)
                        )
                    )
                    this.root.setOnClickListener {
                        interaction.onSelectedCreditCard(item, position, selectedIndexCard)
                        selectedIndexCard = position
                    }
                    if (selectedIndexCard == position) {
                        this.backgroundCreditCard.hide()
                    } else {
                        this.backgroundCreditCard.show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        _context = parent.context
        val inflate = LayoutInflater.from(context)
        val view = inflate.inflate(R.layout.credit_card_item_layout, parent, false)
        return PaymentMethodViewHolder(CreditCardItemLayoutBinding.bind(view))
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int = data.size

    interface PaymentMethodInteraction {
        fun onAddNewCreditCard() {}
        fun onSelectedCreditCard(item: TokenCreditCardEntity, index: Int, lastIndex: Int) {}
    }
}