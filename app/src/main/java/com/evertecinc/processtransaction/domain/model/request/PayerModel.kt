package com.evertecinc.processtransaction.domain.model.request

data class PayerModel(
    val document: String,
    val documentType: String,
    val email: String,
    val mobile: String,
    val name: String,
    val surname: String
)