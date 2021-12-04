package com.evertecinc.processtransaction.domain.client

import com.evertecinc.processtransaction.domain.model.request.*
import com.evertecinc.processtransaction.domain.model.response.CreditCardInformationResModel
import com.evertecinc.processtransaction.domain.model.response.ProcessTransactionResModel
import com.evertecinc.processtransaction.domain.model.response.RequestTransactionResModel
import com.evertecinc.processtransaction.domain.model.response.TokenizeResModel
import retrofit2.http.Body
import retrofit2.http.POST

interface PlaceToPayApi {

    @POST("gateway/information")
    suspend fun checkInformation(@Body model: CreditCardInformationReqModel): CreditCardInformationResModel

    @POST("gateway/process")
    suspend fun processTransaction(@Body model: ProcessTransactionReqModel): ProcessTransactionResModel

    @POST("gateway/search")
    suspend fun searchTransaction(@Body model: SearchTransactionReqModel): SearchTransactionReqModel

    @POST("gateway/query")
    suspend fun requestTransaction(@Body model: RequestTransactionReqModel): RequestTransactionResModel

    @POST("gateway/transaction")
    suspend fun operationOverTransaction(@Body model: ProcessTransactionReqModel): ProcessTransactionResModel

    @POST("gateway/tokenize")
    suspend  fun tokenizeCreditCard(@Body model: TokenizeReqModel): TokenizeResModel
}