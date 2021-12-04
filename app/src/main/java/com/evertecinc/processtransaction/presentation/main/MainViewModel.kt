package com.evertecinc.processtransaction.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.domain.usecases.InformationGatewayUseCase
import com.evertecinc.processtransaction.presentation.states.MainEventState
import com.evertecinc.processtransaction.presentation.states.MainViewStates
import com.evertecinc.processtransaction.presentation.states.TransactionViewStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: InformationGatewayUseCase
) : ViewModel() {
    private val _viewState: MutableLiveData<MainViewStates> = MutableLiveData()
    val viewSate: LiveData<MainViewStates>
        get() = _viewState

    private val channelManager: ChannelDataManager<MainViewStates> =
        object : ChannelDataManager<MainViewStates>(Dispatchers.IO) {
            override fun handleNewData(data: MainViewStates) {
                this@MainViewModel.handleNewData(data = data)
            }

        }

    val shouldDisplayProgressBar: LiveData<Boolean> = channelManager.shouldDisplayProgressBar

    val errorState: LiveData<ErrorState?>
        get() = channelManager.errorStack.errorState

    fun setupChannel() = channelManager.setupChannel()

    private fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return channelManager.isJobAlreadyActive(stateEvent)
    }

    private fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<MainViewStates>?>
    ) =
        channelManager.launchJob(stateEvent, jobFunction)

    private fun emitInvalidStateEvent(stateEvent: StateEvent) = flow {
        emit(
            DataState.error<MainViewStates>(
                errorMessage = Constants.INVALID_STATE_EVENT,
                stateEvent = stateEvent
            )
        )
    }

    fun clearErrorStack() {
        channelManager.clearAllErrorState()
    }

    private fun initNewViewState(): MainViewStates {
        return MainViewStates()
    }

    private fun setViewState(state: MainViewStates) {
        _viewState.postValue(state)
    }

    private fun getCurrentViewStateOrNew(): MainViewStates {
        return viewSate.value ?: initNewViewState()
    }

    fun setStateEvent(stateEvent: StateEvent) {
        if (!isJobAlreadyActive(stateEvent)) {
            val job: Flow<DataState<MainViewStates>> =
                when (stateEvent) {
                    is MainEventState.CardListEvent -> {
                        useCase.fetchTokenizedCreditCards(stateEvent)
                    }
                    is MainEventState.TransactionListEvent -> {
                        useCase.fetchTransactionsList(stateEvent)
                    }
                    else -> {
                        emitInvalidStateEvent(stateEvent)
                    }
                }
            launchJob(stateEvent, job)
        }
    }

    fun handleNewData(data: MainViewStates) {
        data.let { viewState ->
            viewState.transactions.transactions?.let {
                setTransactionList(it)
            }
            viewState.cards.list?.let {
                setCardList(it)
            }
        }
    }

    private fun setCardList(cards: List<TokenCreditCardEntity>) {
        val update = getCurrentViewStateOrNew()
        update.cards.list = cards
        setViewState(update)
    }

    private fun setTransactionList(transactions: List<TransactionWithDetailsRelation>) {
        val update = getCurrentViewStateOrNew()
        update.transactions.transactions = transactions
        setViewState(update)
    }
}