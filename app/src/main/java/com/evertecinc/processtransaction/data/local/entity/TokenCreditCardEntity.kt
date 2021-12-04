package com.evertecinc.processtransaction.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_credit_card")
data class TokenCreditCardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "provider")
    var provider: String = "",
    @ColumnInfo(name = "token")
    var token: String = "",
    @ColumnInfo(name = "sub_token")
    var subToken: String = "",
    @ColumnInfo(name = "franchise")
    var franchise: String = "",
    @ColumnInfo(name = "franchise_name")
    var franchiseName: String = "",
    @ColumnInfo(name = "last_digits")
    var lastDigits: String = "",
    @ColumnInfo(name = "digits")
    var digits: String = "",
    @ColumnInfo(name = "cvv")
    var cvv: String = "",
    @ColumnInfo(name = "valid_until")
    var validUntil: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "sur_name")
    var surName: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "installments")
    var installments: String? = null,
    @ColumnInfo(name = "require_otp")
    var requireOtp: Boolean = false,
    @ColumnInfo(name = "require_cvv")
    var requireCvv: Boolean = true,
    var timestamp: Long? = null
)