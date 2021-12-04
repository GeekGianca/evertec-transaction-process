package com.evertecinc.processtransaction.data.repo

import android.util.Log
import com.evertecinc.processtransaction.core.*
import com.evertecinc.processtransaction.core.Constants.REFRESH_CACHE_TIME
import com.evertecinc.processtransaction.data.local.dao.ProductDao
import com.evertecinc.processtransaction.data.local.dao.PurchaseDetailDao
import com.evertecinc.processtransaction.data.local.dao.TokenCreditCardDao
import com.evertecinc.processtransaction.data.local.dao.TransactionDao
import com.evertecinc.processtransaction.data.local.entity.ProductEntity
import com.evertecinc.processtransaction.data.local.entity.PurchaseDetailEntity
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.TransactionEntity
import com.evertecinc.processtransaction.data.local.entity.relation.TransactionWithDetailsRelation
import com.evertecinc.processtransaction.data.mapper.CreditCardTokenizedMapper
import com.evertecinc.processtransaction.data.mapper.TransactionMapper
import com.evertecinc.processtransaction.domain.client.PlaceToPayApi
import com.evertecinc.processtransaction.domain.model.request.CreditCardInformationReqModel
import com.evertecinc.processtransaction.domain.model.request.ProcessTransactionReqModel
import com.evertecinc.processtransaction.domain.model.request.TokenizeReqModel
import com.evertecinc.processtransaction.domain.model.response.CreditCardInformationResModel
import com.evertecinc.processtransaction.domain.model.response.ProcessTransactionResModel
import com.evertecinc.processtransaction.domain.model.response.TokenizeResModel
import com.evertecinc.processtransaction.domain.usecases.PlaceToPayUseCase
import com.evertecinc.processtransaction.presentation.states.TransactionViewStates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceToPayRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    val client: PlaceToPayApi,
    val productDao: ProductDao,
    val transactionDao: TransactionDao,
    val tokenCreditDao: TokenCreditCardDao,
    val purchaseDetailDao: PurchaseDetailDao,
    val ccMapper: CreditCardTokenizedMapper,
    val txMapper: TransactionMapper
) : PlaceToPayUseCase {
    companion object {
        private const val TAG = "PlaceToPayRepositoryImp"
    }

    override fun fetchProductItem(stateEvent: StateEvent): Flow<DataState<TransactionViewStates>> =
        flow {
            try {
                val product = productDao.selectProductDistinctUntilChanged()
                product.collect {
                    val viewState = TransactionViewStates(
                        itemView = TransactionViewStates.ProductItemViews(it)
                    )
                    emit(DataState.data(data = viewState, stateEvent))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(DataState.error(e.message, stateEvent))
            }
        }

    override fun createTokenizedCreditCard(
        stateEvent: StateEvent,
        model: TokenizeReqModel
    ): Flow<DataState<TransactionViewStates>> {
        return object :
            NetworkBoundResource<TokenizeResModel, TokenCreditCardEntity, TransactionViewStates>(
                dispatcher = dispatcher,
                stateEvent = stateEvent,
                apiCall = { client.tokenizeCreditCard(model) },
                cacheCall = { tokenCreditDao.selectLastInsertCreditCard(model.instrument.card!!.number) }
            ) {
            override suspend fun shouldReturnCache(cacheResult: TokenCreditCardEntity?): Boolean {
                return cacheResult != null
            }

            override suspend fun updateCache(networkObj: TokenizeResModel) {
                withContext(dispatcher) {
                    try {
                        val entity = ccMapper.toEntity(networkObj)
                        //This not recommended, only to test
                        entity.name = model.payer.name
                        entity.surName = model.payer.surname
                        entity.email = model.payer.email
                        entity.digits = model.instrument.card!!.number
                        entity.cvv = model.instrument.card.cvv
                        Log.d(TAG, "Object: $entity")
                        tokenCreditDao.insert(entity)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun returnSuccessCache(
                resultObj: TokenCreditCardEntity,
                stateEvent: StateEvent?
            ): DataState<TransactionViewStates> {
                val viewState = TransactionViewStates(
                    createCardViews = TransactionViewStates.CreateTokenizedCreditCardViews(
                        card = resultObj
                    )
                )
                return DataState.data(data = viewState, stateEvent = stateEvent)
            }

        }.result
    }

    /**
     * This method simulates the process of adding a product to a transaction
     */
    override suspend fun saveProductFake() {
        withContext(dispatcher) {
            try {
                //Save fake info
                val refPurchase = Commons.getRefFormat()
                val item = ProductEntity(
                    1,
                    "Nike Zion 1 PF",
                    refPurchase, //Transaction reference
                    "Notes: Size: EU 40 | Color: Black and Orange",
                    1,
                    758000.0,
                    758000.0,
                    null
                )
                productDao.insert(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun updateProductItem(
        stateEvent: StateEvent,
        event: Int
    ): Flow<DataState<TransactionViewStates>> = flow {
        try {
            if (event == 0)
                productDao.addQuantity()
            else
                productDao.removeQuantity()
            val viewState = TransactionViewStates(
                updateItemViews = TransactionViewStates.UpdateProductItemViews(productDao.selectProduct())
            )
            emit(DataState.data(data = viewState, stateEvent))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.error(e.message, stateEvent))
        }
    }

    override fun findAllTokenizedCreditCards(stateEvent: StateEvent): Flow<DataState<TransactionViewStates>> =
        flow {
            val cacheResult = safeCacheCall(dispatcher) {
                tokenCreditDao.selectAllTokenizedCreditCards()
            }
            emit(
                object : CacheResponseHandler<TransactionViewStates, List<TokenCreditCardEntity>>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: List<TokenCreditCardEntity>): DataState<TransactionViewStates> {
                        val viewState = TransactionViewStates(
                            cardsViews = TransactionViewStates.ListTokenCreditCardsViews(
                                cards = resultObj
                            )
                        )
                        return DataState.data(data = viewState, stateEvent = stateEvent)
                    }

                }.result
            )
        }

    override fun updateMethodPaymentProduct(
        stateEvent: StateEvent,
        cardIdToken: Long
    ): Flow<DataState<TransactionViewStates>> = flow {
        try {
            productDao.updatePaymentMethod(cardIdToken)
            val viewState = TransactionViewStates(
                attachPayment = TransactionViewStates.AttachPaymentMethodViews(
                    status = true
                )
            )
            emit(DataState.data(data = viewState, stateEvent = stateEvent))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.error(e.message, stateEvent))
        }
    }

    override fun fetchInformationCard(
        stateEvent: StateEvent,
        model: CreditCardInformationReqModel
    ): Flow<DataState<TransactionViewStates>> {
        return object :
            NetworkBoundResource<CreditCardInformationResModel, TokenCreditCardEntity, TransactionViewStates>(
                dispatcher = dispatcher,
                stateEvent = stateEvent,
                apiCall = { client.checkInformation(model) },
                cacheCall = { tokenCreditDao.selectLastInsertCreditCard(model.instrument.card!!.number) }
            ) {
            override suspend fun shouldReturnCache(cacheResult: TokenCreditCardEntity?): Boolean {
                return true
            }

            override suspend fun updateCache(networkObj: CreditCardInformationResModel) {
                withContext(dispatcher) {
                    try {
                        val entity =
                            tokenCreditDao.selectLastInsertCreditCard(model.instrument.card!!.number)
                        val currentTime = System.currentTimeMillis() / 1000
                        entity?.installments = networkObj.credits[0].installments.asString()
                        entity?.requireOtp = networkObj.requireOtp
                        entity?.requireCvv = networkObj.requireCvv2
                        entity?.timestamp = currentTime
                        Log.d(TAG, "Object: $entity")
                        entity?.let {
                            tokenCreditDao.update(it)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun returnSuccessCache(
                resultObj: TokenCreditCardEntity,
                stateEvent: StateEvent?
            ): DataState<TransactionViewStates> {
                val viewState = TransactionViewStates(
                    infoCardView = TransactionViewStates.InformationCreditCardViews(
                        info = resultObj
                    )
                )
                return DataState.data(data = viewState, stateEvent = stateEvent)
            }

        }.result
    }

    override fun processPurchaseTransaction(
        stateEvent: StateEvent,
        model: ProcessTransactionReqModel,
        modelProduct: ProductEntity,
        address: String
    ): Flow<DataState<TransactionViewStates>> {
        return object :
            NetworkBoundResource<ProcessTransactionResModel, TransactionWithDetailsRelation, TransactionViewStates>(
                dispatcher = dispatcher,
                stateEvent = stateEvent,
                apiCall = { client.processTransaction(model) },
                cacheCall = { transactionDao.selectTransactionResume() }
            ) {
            override suspend fun shouldReturnCache(cacheResult: TransactionWithDetailsRelation?): Boolean {
                return true
            }

            override suspend fun updateCache(networkObj: ProcessTransactionResModel) {
                try {
                    val entity = txMapper.toEntity(networkObj)
                    entity.installments = model.instrument.card!!.installments
                    val txId = transactionDao.insert(entity)
                    //If there are more than 1 product then it iterates over
                    // the list of details, in this case of technical test
                    // I will do it with only 1 product
                    val details = PurchaseDetailEntity(
                        productName = modelProduct.productName,
                        reference = modelProduct.reference,
                        description = modelProduct.description,
                        quantity = modelProduct.quantity,
                        totalAmount = modelProduct.totalAmount,
                        transactionId = txId,
                        name = model.payer.name + " " + model.payer.surname,
                        address = address
                    )
                    purchaseDetailDao.insert(details)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun returnSuccessCache(
                resultObj: TransactionWithDetailsRelation,
                stateEvent: StateEvent?
            ): DataState<TransactionViewStates> {
                val viewState = TransactionViewStates(
                    transactionViews = TransactionViewStates.ProcessPaymentTransactionViews(
                        transaction = resultObj
                    )
                )
                return DataState.data(data = viewState, stateEvent = stateEvent)
            }

        }.result
    }
}