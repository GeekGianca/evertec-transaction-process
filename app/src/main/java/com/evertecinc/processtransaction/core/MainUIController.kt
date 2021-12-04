package com.evertecinc.processtransaction.core

interface MainUIController {
    fun onDisplayLoading(state: Boolean)
    fun onErrorDisplay(ex: ErrorState)
}