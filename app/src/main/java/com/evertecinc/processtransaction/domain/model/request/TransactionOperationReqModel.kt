package com.evertecinc.processtransaction.domain.model.request

data class TransactionOperationReqModel(
    val auth: AuthModel,
    val authorization: String,
    val internalReference: Int,
    val action: String
)