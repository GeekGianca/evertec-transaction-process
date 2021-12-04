package com.evertecinc.processtransaction.domain.model.request

data class AuthModel(
    val login: String,
    val nonce: String,
    val seed: String,
    val tranKey: String
)