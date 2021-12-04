package com.evertecinc.processtransaction.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.evertecinc.processtransaction.data.local.entity.ProductEntity
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity

data class PurchaseWithTokenCreditCardRelation(
    @Embedded
    val productEntity: ProductEntity,
    @Relation(
        parentColumn = "token_credit_card_id",
        entityColumn = "id"
    )
    val tokenCreditCard: TokenCreditCardEntity?
)