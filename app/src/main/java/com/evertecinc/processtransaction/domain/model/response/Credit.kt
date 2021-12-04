package com.evertecinc.processtransaction.domain.model.response

data class Credit(
    val code: Any,
    val groupCode: String,
    val installments: Int,
    val type: String
)