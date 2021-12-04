package com.evertecinc.processtransaction.domain.model.response

data class Status(
    val date: String,
    val message: String,
    val reason: String,
    val status: String
)