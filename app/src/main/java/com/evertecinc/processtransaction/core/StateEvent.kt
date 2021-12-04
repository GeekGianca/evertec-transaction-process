package com.evertecinc.processtransaction.core

interface StateEvent {
    fun errorInfo(): String
    fun eventName(): String
    fun shouldDisplayProgressBar(): Boolean
}