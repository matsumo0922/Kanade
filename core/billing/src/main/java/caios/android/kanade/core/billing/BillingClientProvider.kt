package caios.android.kanade.core.billing

import android.content.Context
import caios.android.kanade.core.billing.models.BasePlanId
import caios.android.kanade.core.billing.models.ProductDetails
import caios.android.kanade.core.billing.models.ProductId
import caios.android.kanade.core.billing.models.translate
import caios.android.kanade.core.common.network.di.ApplicationScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

typealias ResponseListener<T> = (Result<T>) -> Unit

interface BillingClientProvider {

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        DISPOSED,
        UNAVAILABLE,
    }
}

class BillingClientProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
) : BillingClientProvider {

    private val initializeResponseListeners = mutableListOf<ResponseListener<Unit>>()
    private val compositeListener = CompositePurchasesUpdatedListener()
    private val connectionLock = Object()

    private val billingClient = BillingClient
        .newBuilder(context)
        .setListener(compositeListener)
        .enablePendingPurchases()
        .build()

    private var state = BillingClientProvider.State.DISCONNECTED

    fun initialize(listener: ResponseListener<Unit>) {
        var verificationError: Throwable? = null
        var shouldNotifyOnResponse = false

        synchronized(connectionLock) {
            if (state == BillingClientProvider.State.DISPOSED) {
                verificationError = InitializationFailedException(billingResponse(BillingResponseCode.SERVICE_DISCONNECTED), true)
                return@synchronized
            }

            if (state == BillingClientProvider.State.UNAVAILABLE) {
                verificationError = InitializationFailedException(billingResponse(BillingResponseCode.BILLING_UNAVAILABLE), true)
                return@synchronized
            }

            if (state == BillingClientProvider.State.CONNECTED) {
                shouldNotifyOnResponse = true
                return@synchronized
            }

            initializeResponseListeners.add(listener)

            if (state == BillingClientProvider.State.CONNECTING) {
                return@synchronized
            }

            state = BillingClientProvider.State.CONNECTING

            billingClient.startConnection(
                object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        state = when (val response = billingResult.toResponse()) {
                            is BillingResponse.OK -> {
                                Timber.d("BillingClient connected")
                                initializeResponseListeners.forEach { it.invoke(Result.success(Unit)) }
                                BillingClientProvider.State.CONNECTED
                            }
                            is BillingResponse.BillingUnavailable -> {
                                Timber.d("BillingClient unavailable")
                                initializeResponseListeners.forEach { it.invoke(Result.failure(InitializationFailedException(response))) }
                                BillingClientProvider.State.UNAVAILABLE
                            }
                            else -> {
                                Timber.d("BillingClient connection failed: $response")
                                initializeResponseListeners.forEach { it.invoke(Result.failure(InitializationFailedException(response))) }
                                BillingClientProvider.State.DISPOSED
                            }
                        }

                        initializeResponseListeners.clear()
                    }

                    override fun onBillingServiceDisconnected() {
                        Timber.d("BillingClient disconnected")
                        state = BillingClientProvider.State.DISCONNECTED
                    }
                }
            )
        }

        verificationError?.let {
            listener.invoke(Result.failure(it))
            return
        }

        if (shouldNotifyOnResponse) {
            listener.invoke(Result.success(Unit))
            return
        }
    }

    fun dispose() {
        synchronized(connectionLock) {
            state = BillingClientProvider.State.DISPOSED
        }
    }

    fun queryProductDetails(
        productId: ProductId,
        listener: ResponseListener<ProductDetails?>
    ) {
        queryProductDetailsList(QueryProductDetailsCommand(listOf(productId))) { result ->
            result.fold(
                onSuccess = { list ->
                    list.firstOrNull { it.productId == productId }?.let {
                        listener.invoke(Result.success(it))
                        return@fold
                    }

                    listener.invoke(Result.failure(QueryProductDetailsFailedException(billingResponse(BillingResponseCode.ERROR), productId)))
                },
                onFailure = {
                    listener.invoke(Result.failure(it))
                }
            )
        }
    }

    private fun queryProductDetailsList(
        command: QueryProductDetailsCommand,
        listener: ResponseListener<List<ProductDetails>>
    ) {
        initialize { result ->
            result.fold(
                onSuccess = {
                    billingClient.queryProductDetailsAsync(command.toQueryProductDetailsParams()) { billingResult, productDetailsList ->
                        when (val response = billingResult.toResponse()) {
                            is BillingResponse.OK -> {
                                if (productDetailsList.isEmpty()) {
                                    listener.invoke(Result.failure(InternalQueryProductDetailsListFailedException(response, command)))
                                } else {
                                    listener.invoke(Result.success(productDetailsList.translate()))
                                }
                            }
                            is BillingResponse.ServiceDisconnected, is BillingResponse.ServiceError -> {
                                state = BillingClientProvider.State.DISPOSED
                                listener.invoke(Result.failure(InternalQueryProductDetailsListFailedException(response, command)))
                            }
                            else -> {
                               listener.invoke(Result.failure(InternalQueryProductDetailsListFailedException(response, command)))
                            }
                        }
                    }
                },
                onFailure = {
                    if (it is InitializationFailedException) {
                        listener.invoke(
                            Result.failure(
                                InternalQueryProductDetailsListFailedException(
                                    response = it.response,
                                    command = command,
                                    isFailedOnInitialize = true,
                                    isCalledAfterDispose = it.isCalledAfterDispose,
                                )
                            )
                        )
                    } else {
                        listener.invoke(Result.failure(it))
                    }
                }
            )
        }
    }

    fun queryProductDetails(
        productId: ProductId,
        basePlanId: BasePlanId,
        listener: ResponseListener<ProductDetails?>
    ) {

    }
}
