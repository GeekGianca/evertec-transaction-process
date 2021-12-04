package com.evertecinc.processtransaction.core

import com.evertecinc.processtransaction.data.local.entity.relation.PurchaseWithTokenCreditCardRelation
import com.evertecinc.processtransaction.domain.model.AmountModel
import com.evertecinc.processtransaction.domain.model.request.*

fun PurchaseWithTokenCreditCardRelation.toTransaction(
    total: Double,
    installments: Int
): ProcessTransactionReqModel {
    val payer = PayerModel(
        document = "",
        documentType = "",
        email = this.tokenCreditCard!!.email,
        mobile = "",
        name = this.tokenCreditCard.name,
        surname = this.tokenCreditCard.surName
    )
    val amount = AmountModel(
        currency = "COP",
        total = total
    )
    val payment = PaymentModel(
        amount = amount,
        description = this.productEntity.productName,
        reference = this.productEntity.reference
    )
    val instrument = Instrument(
        /*token = TokenModel(
            franchise = this.tokenCreditCard.franchise,
            franchiseName = this.tokenCreditCard.franchiseName,
            lastDigits = this.tokenCreditCard.lastDigits,
            subtoken = this.tokenCreditCard.subToken,
            token = this.tokenCreditCard.token,
            validUntil = this.tokenCreditCard.validUntil
        )*/
        card = CardModel(
            cvv = this.tokenCreditCard.cvv,
            expiration = this.tokenCreditCard.validUntil.validThuFormat(),
            installments = installments,
            number = this.tokenCreditCard.digits
        )
    )
    return ProcessTransactionReqModel(
        auth = auth,
        instrument = instrument,
        payer = payer,
        payment = payment
    )
}