package com.evertecinc.processtransaction.domain.usecases

import com.evertecinc.processtransaction.core.DataState
import com.evertecinc.processtransaction.core.StateEvent
import com.evertecinc.processtransaction.data.local.entity.ProductEntity
import com.evertecinc.processtransaction.domain.model.request.CreditCardInformationReqModel
import com.evertecinc.processtransaction.domain.model.request.ProcessTransactionReqModel
import com.evertecinc.processtransaction.domain.model.request.TokenizeReqModel
import com.evertecinc.processtransaction.presentation.states.TransactionViewStates
import kotlinx.coroutines.flow.Flow

interface PlaceToPayUseCase {
    fun fetchProductItem(stateEvent: StateEvent): Flow<DataState<TransactionViewStates>>
    suspend fun saveProductFake()
    fun updateProductItem(
        stateEvent: StateEvent,
        event: Int
    ): Flow<DataState<TransactionViewStates>>

    fun createTokenizedCreditCard(
        stateEvent: StateEvent,
        model: TokenizeReqModel
    ): Flow<DataState<TransactionViewStates>>

    fun findAllTokenizedCreditCards(stateEvent: StateEvent): Flow<DataState<TransactionViewStates>>

    fun updateMethodPaymentProduct(
        stateEvent: StateEvent,
        cardIdToken: Long
    ): Flow<DataState<TransactionViewStates>>

    fun fetchInformationCard(
        stateEvent: StateEvent,
        model: CreditCardInformationReqModel
    ): Flow<DataState<TransactionViewStates>>

    fun processPurchaseTransaction(
        stateEvent: StateEvent,
        model: ProcessTransactionReqModel,
        modelProduct: ProductEntity,
        address: String,
    ): Flow<DataState<TransactionViewStates>>
}