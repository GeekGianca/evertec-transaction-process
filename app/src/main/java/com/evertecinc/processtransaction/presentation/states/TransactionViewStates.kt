package com.evertecinc.processtransaction.presentation.states

import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.TransactionEntity
import com.evertecinc.processtransaction.data.local.entity.relation.PurchaseWithTokenCreditCardRelation
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.domain.model.response.CreditCardInformationResModel
import com.evertecinc.processtransaction.domain.model.response.TokenizeResModel

data class TransactionViewStates(
    var itemView: ProductItemViews = ProductItemViews(),
    var infoCardView: InformationCreditCardViews = InformationCreditCardViews(),
    var cardsViews: ListTokenCreditCardsViews = ListTokenCreditCardsViews(),
    var createCardViews: CreateTokenizedCreditCardViews = CreateTokenizedCreditCardViews(),
    var updateItemViews: UpdateProductItemViews = UpdateProductItemViews(),
    var attachPayment: AttachPaymentMethodViews = AttachPaymentMethodViews(),
    var transactionViews: ProcessPaymentTransactionViews = ProcessPaymentTransactionViews()
) {
    data class ProductItemViews(var product: PurchaseWithTokenCreditCardRelation? = null)

    data class InformationCreditCardViews(var info: TokenCreditCardEntity? = null)

    data class ListTokenCreditCardsViews(var cards: List<TokenCreditCardEntity>? = null)

    data class CreateTokenizedCreditCardViews(var card: TokenCreditCardEntity? = null)

    data class UpdateProductItemViews(var product: PurchaseWithTokenCreditCardRelation? = null)

    data class AttachPaymentMethodViews(var status: Boolean? = null)

    data class ProcessPaymentTransactionViews(var transaction: TransactionWithDetailsRelation? = null)
}