package com.evertecinc.processtransaction.core

data class TransactionState(
    var status: String,
    var detail: String,
    var reference: String
)