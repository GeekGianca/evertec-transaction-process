package com.evertecinc.processtransaction.domain.model.request

import com.evertecinc.processtransaction.domain.model.TokenModel

data class InstrumentModel(
    val card: CardModel,
    val token: TokenModel
)