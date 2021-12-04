package com.evertecinc.processtransaction.domain.model.response

data class Additional(
    val batch: Any,
    val bin: String,
    val credit: Credit,
    val expiration: String,
    val iceAmount: Int,
    val installmentAmount: Double,
    val interestAmount: Double,
    val line: Any,
    val merchantCode: String,
    val terminalNumber: String,
    val totalAmount: Double
)