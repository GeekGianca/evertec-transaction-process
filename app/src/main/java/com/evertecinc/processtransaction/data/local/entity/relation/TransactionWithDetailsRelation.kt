package com.evertecinc.processtransaction.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.evertecinc.processtransaction.data.local.entity.PurchaseDetailEntity
import com.evertecinc.processtransaction.data.local.entity.TransactionEntity

data class TransactionWithDetailsRelation(
    @Embedded
    val productEntity: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "transaction_id"
    )
    val details: List<PurchaseDetailEntity>
)