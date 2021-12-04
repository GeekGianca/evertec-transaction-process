package com.evertecinc.processtransaction.domain.usecases

import com.evertecinc.processtransaction.core.DataState
import com.evertecinc.processtransaction.core.StateEvent
import com.evertecinc.processtransaction.presentation.states.MainViewStates

interface InformationGatewayUseCase {

    fun fetchTransactionsList(stateEvent: StateEvent): kotlinx.coroutines.flow.Flow<DataState<MainViewStates>>

    fun fetchTokenizedCreditCards(stateEvent: StateEvent): kotlinx.coroutines.flow.Flow<DataState<MainViewStates>>
}