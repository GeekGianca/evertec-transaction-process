package com.evertecinc.processtransaction.presentation.states

import com.evertecinc.processtransaction.core.Constants.EMPTY_LIST
import com.evertecinc.processtransaction.core.Constants.ERROR_TO_CHANGE_CARD
import com.evertecinc.processtransaction.core.Constants.ERROR_TO_CREATE_TOKEN_CARD
import com.evertecinc.processtransaction.core.Constants.ERROR_TO_PROCESS_TRANSACTION
import com.evertecinc.processtransaction.core.Constants.ERROR_TO_REQUEST_INFO_CARD
import com.evertecinc.processtransaction.core.Constants.ERROR_TO_UPDATE_ITEM
import com.evertecinc.processtransaction.core.StateEvent
import com.evertecinc.processtransaction.data.local.entity.ProductEntity
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.domain.model.request.CreditCardInformationReqModel
import com.evertecinc.processtransaction.domain.model.request.ProcessTransactionReqModel
import com.evertecinc.processtransaction.domain.model.request.TokenizeReqModel

sealed class TransactionProcessEventState : StateEvent {
    object FetchInitialCheckoutEvent : TransactionProcessEventState() {
        override fun errorInfo(): String = ""
        override fun eventName(): String = FetchInitialCheckoutEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class UpdateProductItemEvent(val event: Int) : TransactionProcessEventState() {
        override fun errorInfo(): String = ERROR_TO_UPDATE_ITEM
        override fun eventName(): String = UpdateProductItemEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class CreateTokenizedCreditCardEvent(val model: TokenizeReqModel) :
        TransactionProcessEventState() {
        override fun errorInfo(): String = ERROR_TO_CREATE_TOKEN_CARD
        override fun eventName(): String = CreateTokenizedCreditCardEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    object ListTokenizedCreditCardsEvent : TransactionProcessEventState() {
        override fun errorInfo(): String = EMPTY_LIST
        override fun eventName(): String = ListTokenizedCreditCardsEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class ChangePaymentMethodEvent(val creditCardId: Long) : TransactionProcessEventState() {
        override fun errorInfo(): String = ERROR_TO_CHANGE_CARD
        override fun eventName(): String = ChangePaymentMethodEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true

    }

    class RequestCardInformationEvent(val model: CreditCardInformationReqModel) :
        TransactionProcessEventState() {
        override fun errorInfo(): String = ERROR_TO_REQUEST_INFO_CARD
        override fun eventName(): String = RequestCardInformationEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class ProcessTransactionEvent(
        val model: ProcessTransactionReqModel, val product: ProductEntity,
        val address: String,
    ) :
        TransactionProcessEventState() {
        override fun errorInfo(): String = ERROR_TO_PROCESS_TRANSACTION

        override fun eventName(): String = ProcessTransactionEvent::class.java.name

        override fun shouldDisplayProgressBar(): Boolean = true

    }
}