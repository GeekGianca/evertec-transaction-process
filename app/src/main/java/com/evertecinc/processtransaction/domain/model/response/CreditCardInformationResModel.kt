package com.evertecinc.processtransaction.domain.model.response

data class CreditCardInformationResModel(
    val cardType: String,
    val cardTypes: List<String>,
    val credits: List<CreditInformation>,
    val displayInterest: Boolean,
    val provider: String,
    val requireCvv2: Boolean,
    val requireOtp: Boolean,
    val serviceCode: String,
    val status: Status,
    val threeDS: String
)