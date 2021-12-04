package com.evertecinc.processtransaction.data.local.dao

import androidx.room.*
import com.evertecinc.processtransaction.data.local.entity.TransactionEntity
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Transaction
    @Query("SELECT * FROM `transaction`")
    suspend fun selectTransaction(): TransactionEntity?

    @Transaction
    @Query("SELECT * FROM `transaction` ORDER BY id ASC LIMIT 1")
    suspend fun selectTransactionResume(): TransactionWithDetailsRelation?

    @Transaction
    @Query("SELECT * FROM `transaction`")
    suspend fun selectAllTransactions(): List<TransactionWithDetailsRelation>
}