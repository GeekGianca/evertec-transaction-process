package com.evertecinc.processtransaction.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.core.Util
import com.evertecinc.processtransaction.core.validThuFormat
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.databinding.ItemCardLayoutBinding

class AdapterTokenizedCard(val list: List<TokenCreditCardEntity>) :
    RecyclerView.Adapter<AdapterTokenizedCard.TokenizedViewHolder>() {

    private var _context: Context? = null
    private val context get() = _context!!

    inner class TokenizedViewHolder(val binding: ItemCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TokenCreditCardEntity) {
            binding.apply {
                this.franchiseImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        Util.getImageFromFranchise(item.franchise)
                    )
                )
                this.cardNameAndType.text =
                    context.getString(R.string.franchise_args, item.franchiseName, item.lastDigits)
                this.validDate.text = item.validUntil.validThuFormat()
                this.email.text = item.email
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokenizedViewHolder {
        _context = parent.context
        return TokenizedViewHolder(
            ItemCardLayoutBinding.bind(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_card_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: TokenizedViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}