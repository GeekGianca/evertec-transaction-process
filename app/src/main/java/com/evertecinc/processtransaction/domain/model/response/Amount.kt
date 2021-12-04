package com.evertecinc.processtransaction.domain.model.response

data class Amount(
    val currency: String,
    val taxes: List<Taxe>,
    val total: Int
)