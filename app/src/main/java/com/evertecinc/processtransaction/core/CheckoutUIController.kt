package com.evertecinc.processtransaction.core

interface CheckoutUIController {
    fun onDisplayProgress(state: Boolean)
    fun onErrorDisplay(ex: ErrorState?)
}