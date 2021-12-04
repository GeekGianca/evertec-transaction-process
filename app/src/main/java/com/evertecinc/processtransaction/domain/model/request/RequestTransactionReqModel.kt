package com.evertecinc.processtransaction.domain.model.request

data class RequestTransactionReqModel(
    val auth: AuthModel,
    val internalReference: Int
)