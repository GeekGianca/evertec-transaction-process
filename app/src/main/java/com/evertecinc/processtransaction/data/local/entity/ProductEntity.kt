package com.evertecinc.processtransaction.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_car")
class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "product_name")
    var productName: String,
    @ColumnInfo(name = "reference")
    var reference: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "quantity")
    var quantity: Int,
    @ColumnInfo(name = "unitPrice")
    var unitPrice: Double,
    @ColumnInfo(name = "totalAmount")
    var totalAmount: Double,
    @ColumnInfo(name = "token_credit_card_id")
    var tokenCreditCardId: Long?
)