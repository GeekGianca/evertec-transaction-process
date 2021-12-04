package com.evertecinc.processtransaction.domain.model.response

data class TokenizeResModel(
    val instrument: Instrument,
    val provider: String,
    val status: Status
)