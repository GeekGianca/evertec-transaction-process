package com.evertecinc.processtransaction.presentation.states

import com.evertecinc.processtransaction.core.Constants.EMPTY_LIST
import com.evertecinc.processtransaction.core.Constants.EMPTY_TRANSACTION_LIST
import com.evertecinc.processtransaction.core.StateEvent

sealed class MainEventState : StateEvent {
    object CardListEvent : MainEventState() {
        override fun errorInfo(): String = EMPTY_LIST
        override fun eventName(): String = CardListEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    object TransactionListEvent : MainEventState() {
        override fun errorInfo(): String = EMPTY_TRANSACTION_LIST
        override fun eventName(): String = TransactionListEvent::class.java.name
        override fun shouldDisplayProgressBar(): Boolean = true
    }
}