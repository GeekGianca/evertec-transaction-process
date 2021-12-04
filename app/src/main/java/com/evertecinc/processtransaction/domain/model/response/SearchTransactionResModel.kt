package com.evertecinc.processtransaction.domain.model.response

data class SearchTransactionResModel(
    val status: Status,
    val transactions: List<Transaction>
)