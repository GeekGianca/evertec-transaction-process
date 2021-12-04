package com.evertecinc.processtransaction.data.local.dao

import androidx.room.*
import com.evertecinc.processtransaction.data.local.entity.ProductEntity
import com.evertecinc.processtransaction.data.local.entity.relation.PurchaseWithTokenCreditCardRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg product: ProductEntity)

    @Transaction
    @Query("UPDATE shopping_car SET quantity = quantity + 1 WHERE id = 1")
    abstract suspend fun addQuantity()

    @Transaction
    @Query("UPDATE shopping_car SET quantity = quantity - 1 WHERE id = 1 AND quantity > 1")
    abstract suspend fun removeQuantity()

    @Transaction
    @Query("UPDATE shopping_car SET token_credit_card_id =:tokenCardId WHERE id = 1")
    abstract suspend fun updatePaymentMethod(tokenCardId: Long)

    @Transaction
    @Query("SELECT * FROM shopping_car WHERE id = 1")
    abstract fun selectProductTransaction(): Flow<PurchaseWithTokenCreditCardRelation>

    fun selectProductDistinctUntilChanged() =
        selectProductTransaction().distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM shopping_car WHERE id = 1")
    abstract suspend fun selectProduct(): PurchaseWithTokenCreditCardRelation

    @Query("DELETE FROM shopping_car")
    abstract suspend fun deleteShoppingCar()
}