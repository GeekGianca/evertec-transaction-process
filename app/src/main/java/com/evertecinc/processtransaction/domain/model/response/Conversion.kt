package com.evertecinc.processtransaction.domain.model.response

data class Conversion(
    val factor: Double,
    val from: From,
    val to: To
)