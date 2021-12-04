package com.evertecinc.processtransaction.data.mapper

import com.evertecinc.processtransaction.data.local.entity.TransactionEntity
import com.evertecinc.processtransaction.domain.model.response.ProcessTransactionResModel
import javax.inject.Inject

class TransactionMapper @Inject constructor() :
    EntityMapper<TransactionEntity, ProcessTransactionResModel> {
    override fun toModel(entity: TransactionEntity): ProcessTransactionResModel? = null

    override fun toEntity(model: ProcessTransactionResModel): TransactionEntity =
        TransactionEntity(
            date = model.date,
            transactionDate = model.transactionDate,
            internalReference = model.internalReference,
            reference = model.reference,
            paymentMethod = model.paymentMethod,
            franchise = model.franchise,
            franchiseName = model.franchiseName,
            total = model.amount.total,
            authorization = model.authorization,
            receipt = model.receipt,
            type = model.type,
            refunded = model.refunded,
            lastDigits = model.lastDigits,
            provider = model.provider,
            status = model.status.status
        )
}