package com.evertecinc.processtransaction.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_transaction")
data class PurchaseDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "product_name")
    var productName: String = "",
    @ColumnInfo(name = "reference")
    var reference: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,
    @ColumnInfo(name = "totalAmount")
    var totalAmount: Double = 0.0,
    @ColumnInfo(name = "transaction_id")
    var transactionId: Long = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "address")
    var address: String = ""
)