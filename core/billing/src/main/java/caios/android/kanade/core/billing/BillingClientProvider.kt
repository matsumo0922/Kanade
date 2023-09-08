package caios.android.kanade.core.billing

import android.content.Context
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface BillingClientProvider {
    fun startConnection()
    fun terminateConnection()
}

class BillingClientProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
): BillingClientProvider {

    private val billingClient = BillingClient
        .newBuilder(context)
        .enablePendingPurchases()
        .build()

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    override fun startConnection() {
        if (billingClient.isReady) {
            Timber.d("BillingClient is already ready")
            return
        }

        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Timber.d("BillingClient start connection successful")
                    } else {
                        Timber.d("BillingClient start connection failed")
                    }
                }

                override fun onBillingServiceDisconnected() {
                    Timber.d("BillingClient disconnected")

                    scope.launch {
                        delay(5000)
                        startConnection()
                    }
                }
            }
        )
    }

    override fun terminateConnection() {
        billingClient.endConnection()
    }
}
