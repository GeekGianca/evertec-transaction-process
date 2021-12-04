package com.evertecinc.processtransaction.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResumeTransactionModel(
    val reference: String,
    val franchise: String,
    val date: String,
    val status: String,
    val name: String,
    val shipmentAddress: String,
    val detail: String,
    val quantity: Int,
    val totalAmount: Double,
    val lastDigits: String
) : Parcelable