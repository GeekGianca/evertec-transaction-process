package com.evertecinc.processtransaction.domain.model.request

data class CreditCardInformationReqModel(
    val auth: AuthModel,
    val instrument: Instrument,
    val payment: PaymentModel,
    val ipAddress: String = "127.0.0.1",
    val userAgent: String = "Testing"
)