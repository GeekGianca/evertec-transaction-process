package com.evertecinc.processtransaction.domain.model.request

data class CardModel(
    val cvv: String = "",
    val expiration: String = "",
    val installments: Int = 0,
    val number: String = ""
)