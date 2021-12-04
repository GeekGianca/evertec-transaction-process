package com.evertecinc.processtransaction.data.repo

import com.evertecinc.processtransaction.core.CacheResponseHandler
import com.evertecinc.processtransaction.core.DataState
import com.evertecinc.processtransaction.core.StateEvent
import com.evertecinc.processtransaction.core.safeCacheCall
import com.evertecinc.processtransaction.data.local.dao.TokenCreditCardDao
import com.evertecinc.processtransaction.data.local.dao.TransactionDao
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.domain.client.PlaceToPayApi
import com.evertecinc.processtransaction.domain.usecases.InformationGatewayUseCase
import com.evertecinc.processtransaction.presentation.states.MainViewStates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceToPayInfoRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    val client: PlaceToPayApi,
    val transactionDao: TransactionDao,
    val tokenCreditDao: TokenCreditCardDao
) : InformationGatewayUseCase {
    companion object {
        private const val TAG = "PlaceToPayInfoRepositor"
    }

    override fun fetchTokenizedCreditCards(stateEvent: StateEvent): kotlinx.coroutines.flow.Flow<DataState<MainViewStates>> =
        flow {
            val cacheResult = safeCacheCall(dispatcher) {
                tokenCreditDao.selectAllTokenizedCreditCards()
            }
            emit(
                object : CacheResponseHandler<MainViewStates, List<TokenCreditCardEntity>>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: List<TokenCreditCardEntity>): DataState<MainViewStates> {
                        val viewState = MainViewStates(
                            cards = MainViewStates.CardListViews(
                                list = resultObj
                            )
                        )
                        return DataState.data(data = viewState, stateEvent = stateEvent)
                    }

                }.result
            )
        }

    override fun fetchTransactionsList(stateEvent: StateEvent): kotlinx.coroutines.flow.Flow<DataState<MainViewStates>> =
        flow {
            val cacheResult = safeCacheCall(dispatcher) {
                transactionDao.selectAllTransactions()
            }

            emit(
                object : CacheResponseHandler<MainViewStates, List<TransactionWithDetailsRelation>>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: List<TransactionWithDetailsRelation>): DataState<MainViewStates> {
                        val viewState = MainViewStates(
                            transactions = MainViewStates.TransactionListViews(
                                transactions = resultObj
                            )
                        )
                        return DataState.data(data = viewState, stateEvent = stateEvent)
                    }

                }.result
            )
        }

}