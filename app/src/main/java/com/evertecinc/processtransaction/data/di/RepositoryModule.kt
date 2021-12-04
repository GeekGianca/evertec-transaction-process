package com.evertecinc.processtransaction.data.di

import com.evertecinc.processtransaction.data.local.dao.ProductDao
import com.evertecinc.processtransaction.data.local.dao.PurchaseDetailDao
import com.evertecinc.processtransaction.data.local.dao.TokenCreditCardDao
import com.evertecinc.processtransaction.data.local.dao.TransactionDao
import com.evertecinc.processtransaction.data.mapper.CreditCardTokenizedMapper
import com.evertecinc.processtransaction.data.mapper.TransactionMapper
import com.evertecinc.processtransaction.data.repo.PlaceToPayInfoRepositoryImpl
import com.evertecinc.processtransaction.data.repo.PlaceToPayRepositoryImpl
import com.evertecinc.processtransaction.domain.client.PlaceToPayApi
import com.evertecinc.processtransaction.domain.usecases.InformationGatewayUseCase
import com.evertecinc.processtransaction.domain.usecases.PlaceToPayUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providePlaceToPayUseCase(
        client: PlaceToPayApi,
        dao: ProductDao,
        transactionDao: TransactionDao,
        tokenCreditCardDao: TokenCreditCardDao,
        purchaseDetailDao: PurchaseDetailDao,
        creditCardTokenizedMapper: CreditCardTokenizedMapper,
        txMapper: TransactionMapper
    ): PlaceToPayUseCase =
        PlaceToPayRepositoryImpl(
            client = client,
            productDao = dao,
            transactionDao = transactionDao,
            tokenCreditDao = tokenCreditCardDao,
            purchaseDetailDao = purchaseDetailDao,
            ccMapper = creditCardTokenizedMapper,
            txMapper = txMapper
        )

    @Singleton
    @Provides
    fun providePlaceToPayInformationUseCase(
        client: PlaceToPayApi, transactionDao: TransactionDao,
        tokenCreditCardDao: TokenCreditCardDao
    ): InformationGatewayUseCase = PlaceToPayInfoRepositoryImpl(
        client = client,
        transactionDao = transactionDao,
        tokenCreditDao = tokenCreditCardDao
    )

}