package com.evertecinc.processtransaction.domain.model.request

import com.evertecinc.processtransaction.domain.model.AmountModel

data class SearchTransactionReqModel(
    val amount: AmountModel,
    val auth: AuthModel,
    val reference: String
)