package com.evertecinc.processtransaction.domain.model.request

import com.evertecinc.processtransaction.domain.model.TokenModel

data class Instrument(
    val card: CardModel? = null,
    val token: TokenModel? = null
)
