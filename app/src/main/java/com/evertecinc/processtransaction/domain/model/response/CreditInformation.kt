package com.evertecinc.processtransaction.domain.model.response

data class CreditInformation(
    val code: String,
    val description: String,
    val groupCode: String,
    val installments: List<Int>,
    val type: String
)