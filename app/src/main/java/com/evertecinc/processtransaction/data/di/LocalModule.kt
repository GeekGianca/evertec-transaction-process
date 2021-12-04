package com.evertecinc.processtransaction.data.di

import android.content.Context
import com.evertecinc.processtransaction.data.local.DbSchemaLocal
import com.evertecinc.processtransaction.data.local.dao.ProductDao
import com.evertecinc.processtransaction.data.local.dao.PurchaseDetailDao
import com.evertecinc.processtransaction.data.local.dao.TokenCreditCardDao
import com.evertecinc.processtransaction.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctx: Context): DbSchemaLocal =
        DbSchemaLocal.getInstance(ctx)

    @Singleton
    @Provides
    fun provideProductDao(schema: DbSchemaLocal): ProductDao =
        schema.productDao()

    @Singleton
    @Provides
    fun provideTokenCreditCardDao(schema: DbSchemaLocal): TokenCreditCardDao =
        schema.tokenCreditCardDao()

    @Singleton
    @Provides
    fun provideTransactionDao(schema: DbSchemaLocal): TransactionDao =
        schema.transactionDao()

    @Singleton
    @Provides
    fun providePurchaseDetailDao(schema: DbSchemaLocal): PurchaseDetailDao =
        schema.purchaseDetailDao()
}