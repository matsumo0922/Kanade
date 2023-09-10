package caios.android.kanade.core.billing.usecase

import caios.android.kanade.core.billing.PurchaseSingleCommand
import caios.android.kanade.core.billing.models.ProductDetails
import com.android.billingclient.api.Purchase

data class PurchaseConsumableResult(
    val command: PurchaseSingleCommand,
    val productDetails: ProductDetails,
    val purchase: Purchase,
)
