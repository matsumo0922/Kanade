package caios.android.kanade.core.billing.usecase

import android.app.Activity
import caios.android.kanade.core.billing.BillingClient
import caios.android.kanade.core.billing.ConsumeResult
import caios.android.kanade.core.billing.models.ProductDetails
import caios.android.kanade.core.billing.models.ProductItem
import caios.android.kanade.core.billing.models.ProductType
import caios.android.kanade.core.billing.purchaseSingle
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseDonateUseCase @Inject constructor(
    private val billingClient: BillingClient,
    @Dispatcher(KanadeDispatcher.Main) private val mainDispatcher: CoroutineDispatcher,
) {
    suspend fun execute(activity: Activity, productType: ProductType): PurchaseConsumableResult {
        val productDetails = billingClient.queryProductDetails(ProductItem.plus, productType)
        val purchaseResult = purchase(activity, productDetails)

        // TODO: verification purchaseResult

        consume(purchaseResult)

        return purchaseResult
    }

    private suspend fun purchase(
        activity: Activity,
        productDetails: ProductDetails,
    ) : PurchaseConsumableResult = withContext(mainDispatcher) {
        val command = purchaseSingle(productDetails, null)
        val result = billingClient.launchBillingFlow(activity, command)

        PurchaseConsumableResult(command, productDetails, result.billingPurchase)
    }

    private suspend fun consume(
        purchaseConsumableResult: PurchaseConsumableResult
    ) : ConsumeResult {
        return billingClient.consumePurchase(purchaseConsumableResult.purchase)
    }
}
