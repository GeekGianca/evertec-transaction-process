package com.evertecinc.processtransaction.domain.model.request

import com.evertecinc.processtransaction.domain.model.AmountModel

data class PaymentModel(
    val amount: AmountModel,
    val description: String,
    val reference: String
)