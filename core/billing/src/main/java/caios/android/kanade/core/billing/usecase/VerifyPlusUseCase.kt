package caios.android.kanade.core.billing.usecase

import caios.android.kanade.core.billing.BillingClient
import caios.android.kanade.core.billing.models.ProductItem
import caios.android.kanade.core.billing.models.ProductType
import javax.inject.Inject

class VerifyPlusUseCase @Inject constructor(
    private val billingClient: BillingClient,
) {
    suspend fun execute(): Boolean {
        billingClient.queryPurchaseHistory(ProductType.INAPP)

        val productDetails = billingClient.queryProductDetails(ProductItem.plus, ProductType.INAPP)
        val purchases = billingClient.queryPurchases(ProductType.INAPP)

        return purchases.any { it.products.contains(productDetails.productId.toString()) }
    }
}
