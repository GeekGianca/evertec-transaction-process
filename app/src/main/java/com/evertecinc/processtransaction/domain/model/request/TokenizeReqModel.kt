package com.evertecinc.processtransaction.domain.model.request

data class TokenizeReqModel(
    val auth: AuthModel,
    val instrument: Instrument,
    val payer: PayerModel,
    val ipAddress: String = "127.0.0.1",
    val userAgent: String = "Testing"
)