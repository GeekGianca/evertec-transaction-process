package com.evertecinc.processtransaction.core

object Constants {
    const val UNKNOWN_ERROR = "Unknown error"

    const val NETWORK_ERROR = "Network error"

    const val NETWORK_TIMEOUT = 3000L // ms (request will timeout)

    const val NETWORK_ERROR_TIMEOUT = "Network timeout"

    const val NETWORK_ERROR_OFFLINE = "No address associated with hostname"

    const val CACHE_TIMEOUT = 3000L // ms (request will timeout)

    const val CACHE_ERROR_TIMEOUT = "Cache timeout"

    const val REFRESH_CACHE_TIME = 1200L // 20 mins

    const val INVALID_STATE_EVENT = "Invalid state event"

    const val EMPTY_LIST = "Failed to retrieve credit card list"

    const val EMPTY_TRANSACTION_LIST = "Failed to retrieve transaction list"

    const val ERROR_TO_UPDATE_ITEM = "Error to update item product"

    const val ERROR_TO_CREATE_TOKEN_CARD = "Failed to create tokenized credit card"

    const val ERROR_TO_CHANGE_CARD = "Failed to change credit card"

    const val ERROR_TO_REQUEST_INFO_CARD = "Failed to get card information"

    const val ERROR_TO_PROCESS_TRANSACTION = "Error trying to process transaction"

    /**
     * Franchises
     */
    const val VISA = "visa"
    const val VISA_ELECTRON = "visa_electron"
    const val MASTER = "master"
    const val CODENSA = "codensa"
    const val DINERS = "diners"
    const val RIS = "ris"
    const val AMEX = "amex"

    /**
     * Status transactions
     */
    const val OK = "OK"
    const val FAILED = "FAILED"
    const val APPROVED = "APPROVED"
    const val APPROVED_PARTIAL = "APPROVED_PARTIAL"
    const val PARTIAL_EXPIRED = "PARTIAL_EXPIRED"
    const val REJECTED = "REJECTED"
    const val PENDING = "PENDING"
    const val PENDING_VALIDATION = "PENDING_VALIDATION"
    const val PENDING_PROCESS = "PENDING_PROCESS"
    const val REFUNDED = "REFUNDED"
    const val REVERSED = "REVERSED"
    const val ERROR = "ERROR"
    const val UNKNOWN = "UNKNOWN"
    const val MANUAL = "MANUAL"
    const val DISPUTE = "DISPUTE"

    /**
     * Common constants
     */
    const val FROM = "from"
    const val TXT_DETAIL = "txDetail"
}