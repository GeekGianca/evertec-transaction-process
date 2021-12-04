package com.evertecinc.processtransaction.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.databinding.ItemTransactionLayoutBinding

class AdapterTransaction(
    val list: List<TransactionWithDetailsRelation>,
    val interaction: TransactionInteraction
) :
    RecyclerView.Adapter<AdapterTransaction.TransactionViewHolder>() {
    private var _context: Context? = null
    private val context get() = _context!!

    inner class TransactionViewHolder(val binding: ItemTransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionWithDetailsRelation) {
            binding.apply {
                this.franchiseImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        Util.getImageFromFranchise(item.productEntity.franchise)
                    )
                )
                this.cardNameAndType.text =
                    context.getString(
                        R.string.franchise_args,
                        item.productEntity.franchiseName,
                        item.productEntity.lastDigits
                    )
                this.validDate.text = item.productEntity.transactionDate.asFormatUser()
                this.reference.text = item.productEntity.reference
                this.installmentsTv.text =
                    context.getString(R.string.installments_args, item.productEntity.installments)
                binding.status.text = item.productEntity.status.capitalize()
                val color = when (item.productEntity.status) {
                    Constants.APPROVED -> {
                        ContextCompat.getColor(context, R.color.success)
                    }
                    Constants.REJECTED -> {
                        ContextCompat.getColor(context, R.color.error)
                    }
                    else -> {
                        ContextCompat.getColor(context, R.color.checkout)
                    }
                }
                binding.status.setTextColor(color)
                binding.root.setOnClickListener {
                    interaction.onSelectTransaction(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        _context = parent.context
        return TransactionViewHolder(
            ItemTransactionLayoutBinding.bind(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_transaction_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface TransactionInteraction {
        fun onSelectTransaction(item: TransactionWithDetailsRelation)
    }
}