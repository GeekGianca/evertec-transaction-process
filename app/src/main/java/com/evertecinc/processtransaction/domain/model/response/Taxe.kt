package com.evertecinc.processtransaction.domain.model.response

data class Taxe(
    val amount: Int,
    val base: Int,
    val kind: String
)