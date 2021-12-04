package com.evertecinc.processtransaction.presentation.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evertecinc.processtransaction.core.ChannelDataManager
import com.evertecinc.processtransaction.core.Constants.INVALID_STATE_EVENT
import com.evertecinc.processtransaction.core.DataState
import com.evertecinc.processtransaction.core.ErrorState
import com.evertecinc.processtransaction.core.StateEvent
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.TransactionEntity
import com.evertecinc.processtransaction.data.local.entity.relation.PurchaseWithTokenCreditCardRelation
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.domain.usecases.PlaceToPayUseCase
import com.evertecinc.processtransaction.presentation.states.TransactionProcessEventState
import com.evertecinc.processtransaction.presentation.states.TransactionViewStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProcessTransactionViewModel @Inject constructor(private val useCase: PlaceToPayUseCase) :
    ViewModel() {

    //This variable controls the processes of the views,
    // it may or may not contain multiple objects within it
    private val _viewState: MutableLiveData<TransactionViewStates> = MutableLiveData()
    val viewSate: LiveData<TransactionViewStates>
        get() = _viewState

    //It is the information conductor between the data flow channel,
    // which composes the view model between the views and the resulting objects.
    //The result of this process is launched in the viewModel
    // by an object listener within the management channel.
    private val channelManager: ChannelDataManager<TransactionViewStates> =
        object : ChannelDataManager<TransactionViewStates>(Dispatchers.IO) {
            override fun handleNewData(data: TransactionViewStates) {
                this@ProcessTransactionViewModel.handleNewData(data = data)
            }

        }

    //Control the loading bar within the app
    val shouldDisplayProgressBar: LiveData<Boolean> = channelManager.shouldDisplayProgressBar

    //Gets errors that may or may not be
    // caught within the repository or flow statuses
    val errorState: LiveData<ErrorState?>
        get() = channelManager.errorStack.errorState

    //Set the data flow channel inside the viewModel
    fun setupChannel() = channelManager.setupChannel()

    //Controls the sending of information between the view
    // and the viewModel, it depends on the status of the event
    // that is sent for an action to be executed
    fun setStateEvent(stateEvent: StateEvent) {
        if (!isJobAlreadyActive(stateEvent)) {
            val job: Flow<DataState<TransactionViewStates>> =
                when (stateEvent) {
                    is TransactionProcessEventState.FetchInitialCheckoutEvent -> {
                        useCase.fetchProductItem(stateEvent)
                    }
                    is TransactionProcessEventState.UpdateProductItemEvent -> {
                        useCase.updateProductItem(stateEvent, stateEvent.event)
                    }
                    is TransactionProcessEventState.CreateTokenizedCreditCardEvent -> {
                        useCase.createTokenizedCreditCard(stateEvent, stateEvent.model)
                    }
                    is TransactionProcessEventState.ListTokenizedCreditCardsEvent -> {
                        useCase.findAllTokenizedCreditCards(stateEvent)
                    }
                    is TransactionProcessEventState.ChangePaymentMethodEvent -> {
                        useCase.updateMethodPaymentProduct(stateEvent, stateEvent.creditCardId)
                    }
                    is TransactionProcessEventState.RequestCardInformationEvent -> {
                        useCase.fetchInformationCard(stateEvent, stateEvent.model)
                    }
                    is TransactionProcessEventState.ProcessTransactionEvent -> {
                        useCase.processPurchaseTransaction(
                            stateEvent,
                            stateEvent.model,
                            stateEvent.product,
                            stateEvent.address
                        )
                    }
                    else -> {
                        emitInvalidStateEvent(stateEvent)
                    }
                }
            launchJob(stateEvent, job)
        }
    }

    //It is the response to the operations executed within the
    // repository or any other event within the management channel
    fun handleNewData(data: TransactionViewStates) {
        Log.d("TransactionViewStates", "$data")
        data.let { viewState ->
            viewState.itemView.product?.let {
                setProductItem(it)
            }
            viewState.updateItemViews.product?.let {
                setProductItem(it)
            }
            viewState.infoCardView.info?.let {
                setTokenCreditCardInfo(it)
            }
            viewState.cardsViews.cards?.let {
                setCardList(it)
            }
            viewState.createCardViews.card?.let {
                setCreateCreditCard(it)
            }
            viewState.attachPayment.status?.let {
                setAttachPayment(it)
            }
            viewState.transactionViews.transaction?.let {
                setTransactionProcess(it)
            }
        }
    }

    private fun setTransactionProcess(it: TransactionWithDetailsRelation) {
        val update = getCurrentViewStateOrNew()
        update.transactionViews.transaction = it
        setViewState(update)
    }

    private fun setTokenCreditCardInfo(info: TokenCreditCardEntity) {
        val update = getCurrentViewStateOrNew()
        update.infoCardView.info = info
        setViewState(update)
    }

    private fun setAttachPayment(status: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.attachPayment.status = status
        setViewState(update)
    }

    private fun setCreateCreditCard(creditCardEntity: TokenCreditCardEntity) {
        val update = getCurrentViewStateOrNew()
        update.createCardViews.card = creditCardEntity
        setViewState(update)
    }

    private fun setCardList(cards: List<TokenCreditCardEntity>) {
        val update = getCurrentViewStateOrNew()
        update.cardsViews.cards = cards
        setViewState(update)
    }

    private fun setProductItem(item: PurchaseWithTokenCreditCardRelation) {
        val update = getCurrentViewStateOrNew()
        update.itemView.product = item
        setViewState(update)
    }

    private fun setViewState(state: TransactionViewStates) {
        _viewState.postValue(state)
    }

    private fun getCurrentViewStateOrNew(): TransactionViewStates {
        return viewSate.value ?: initNewViewState()
    }

    fun removeAddCardViewState() {
        val update = getCurrentViewStateOrNew()
        update.attachPayment.status = null
        setViewState(update)
    }

    fun removeCreateCardViewState() {
        val update = getCurrentViewStateOrNew()
        update.createCardViews.card = null
        setViewState(update)
    }

    private fun initNewViewState(): TransactionViewStates {
        return TransactionViewStates()
    }

    private fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return channelManager.isJobAlreadyActive(stateEvent)
    }

    private fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<TransactionViewStates>?>
    ) =
        channelManager.launchJob(stateEvent, jobFunction)

    private fun emitInvalidStateEvent(stateEvent: StateEvent) = flow {
        emit(
            DataState.error<TransactionViewStates>(
                errorMessage = INVALID_STATE_EVENT,
                stateEvent = stateEvent
            )
        )
    }

    fun clearErrorStack() {
        channelManager.clearAllErrorState()
    }

    fun fakeInsertProductItem() {
        viewModelScope.launch {
            useCase.saveProductFake()
        }
    }

    fun clearAllViewStates() {
        val update = getCurrentViewStateOrNew()
        update.itemView.product = null
        update.infoCardView.info = null
        update.cardsViews.cards = null
        update.createCardViews.card = null
        update.updateItemViews.product = null
        update.attachPayment.status = null
        update.transactionViews.transaction = null
        setViewState(update)
    }
}