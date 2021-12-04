package com.evertecinc.processtransaction.domain.model

data class TokenModel(
    val franchise: String,
    val franchiseName: String,
    val lastDigits: String,
    val subtoken: String,
    val token: String,
    val validUntil: String
)