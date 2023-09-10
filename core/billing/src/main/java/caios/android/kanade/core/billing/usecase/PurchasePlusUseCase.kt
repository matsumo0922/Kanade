package caios.android.kanade.core.billing.usecase

import android.app.Activity
import caios.android.kanade.core.billing.AcknowledgeResult
import caios.android.kanade.core.billing.BillingClient
import caios.android.kanade.core.billing.models.ProductDetails
import caios.android.kanade.core.billing.models.ProductItem
import caios.android.kanade.core.billing.models.ProductType
import caios.android.kanade.core.billing.purchaseSingle
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchasePlusUseCase @Inject constructor(
    private val billingClient: BillingClient,
    @Dispatcher(KanadeDispatcher.Main) private val mainDispatcher: CoroutineDispatcher,
) {
    suspend fun execute(activity: Activity): PurchaseConsumableResult {
        val productDetails = billingClient.queryProductDetails(ProductItem.plus, ProductType.INAPP)
        val purchaseResult = purchase(activity, productDetails)

        acknowledge(purchaseResult.purchase)

        return purchaseResult
    }

    private suspend fun purchase(
        activity: Activity,
        productDetails: ProductDetails,
    ): PurchaseConsumableResult = withContext(mainDispatcher) {
        val command = purchaseSingle(productDetails, null)
        val result = billingClient.launchBillingFlow(activity, command)

        PurchaseConsumableResult(command, productDetails, result.billingPurchase)
    }

    private suspend fun acknowledge(purchase: Purchase): AcknowledgeResult {
        return billingClient.acknowledgePurchase(purchase)
    }
}
