package com.evertecinc.processtransaction.data.mapper

import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.domain.model.response.TokenizeResModel
import javax.inject.Inject

class CreditCardTokenizedMapper @Inject constructor() :
    EntityMapper<TokenCreditCardEntity, TokenizeResModel> {
    override fun toModel(entity: TokenCreditCardEntity): TokenizeResModel? = null

    override fun toEntity(model: TokenizeResModel): TokenCreditCardEntity =
        TokenCreditCardEntity(
            provider = model.provider,
            token = model.instrument.token.token,
            subToken = model.instrument.token.subtoken,
            franchise = model.instrument.token.franchise,
            franchiseName = model.instrument.token.franchiseName,
            lastDigits = model.instrument.token.lastDigits,
            validUntil = model.instrument.token.validUntil
        )
}