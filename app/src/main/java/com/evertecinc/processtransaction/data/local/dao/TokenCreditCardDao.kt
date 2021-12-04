package com.evertecinc.processtransaction.data.local.dao

import androidx.room.*
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class TokenCreditCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg credit: TokenCreditCardEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(vararg credit: TokenCreditCardEntity)

    @Transaction
    @Query("SELECT * FROM token_credit_card WHERE digits =:digit")
    abstract suspend fun selectLastInsertCreditCard(digit: String): TokenCreditCardEntity?

    @Transaction
    @Query("SELECT * FROM token_credit_card")
    abstract suspend fun selectAllTokenizedCreditCards(): List<TokenCreditCardEntity>

}