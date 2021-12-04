package com.evertecinc.processtransaction.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.evertecinc.processtransaction.data.local.entity.PurchaseDetailEntity

@Dao
interface PurchaseDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg productDetail: PurchaseDetailEntity)
}