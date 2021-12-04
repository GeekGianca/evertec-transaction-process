package com.evertecinc.processtransaction.domain.model.response

data class Transaction(
    val additional: Additional,
    val amount: Amount,
    val authorization: Any,
    val conversion: Conversion,
    val date: String,
    val discount: Any,
    val franchise: String,
    val franchiseName: String,
    val internalReference: Int,
    val issuerName: String,
    val lastDigits: String,
    val paymentMethod: String,
    val processorFields: ProcessorFields,
    val provider: String,
    val receipt: Any,
    val reference: String,
    val refunded: Boolean,
    val status: Status,
    val transactionDate: String,
    val type: String
)