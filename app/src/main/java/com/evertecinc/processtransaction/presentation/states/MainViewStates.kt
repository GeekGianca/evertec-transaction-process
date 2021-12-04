package com.evertecinc.processtransaction.presentation.states

import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation

data class MainViewStates(
    var transactions: TransactionListViews = TransactionListViews(),
    var cards: CardListViews = CardListViews()
) {
    data class TransactionListViews(var transactions: List<TransactionWithDetailsRelation>? = null)
    data class CardListViews(var list: List<TokenCreditCardEntity>? = null)
}