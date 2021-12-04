package com.evertecinc.processtransaction.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "transaction_date")
    var transactionDate: String = "",
    @ColumnInfo(name = "internal_reference")
    var internalReference: Int = 0,
    @ColumnInfo(name = "reference")
    var reference: String = "",
    @ColumnInfo(name = "payment_method")
    var paymentMethod: String = "",
    @ColumnInfo(name = "franchise")
    var franchise: String = "",
    @ColumnInfo(name = "franchise_name")
    var franchiseName: String = "",
    @ColumnInfo(name = "total")
    var total: Double = 0.0,
    @ColumnInfo(name = "authorization")
    var authorization: String = "",
    @ColumnInfo(name = "receipt")
    var receipt: String = "",
    @ColumnInfo(name = "type")
    var type: String = "",
    @ColumnInfo(name = "refunded")
    var refunded: Boolean = false,
    @ColumnInfo(name = "last_digits")
    var lastDigits: String = "",
    @ColumnInfo(name = "provider")
    var provider: String = "",
    @ColumnInfo(name = "status")
    var status: String = "",
    @ColumnInfo(name = "installments")
    var installments: Int = 0
)