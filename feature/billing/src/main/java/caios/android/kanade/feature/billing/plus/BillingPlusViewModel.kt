package caios.android.kanade.feature.billing.plus

import android.app.Activity
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.billing.BillingClient
import caios.android.kanade.core.billing.models.ProductDetails
import caios.android.kanade.core.billing.models.ProductItem
import caios.android.kanade.core.billing.models.ProductType
import caios.android.kanade.core.billing.usecase.ConsumePlusUseCase
import caios.android.kanade.core.billing.usecase.PurchasePlusUseCase
import caios.android.kanade.core.billing.usecase.VerifyPlusUseCase
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import com.android.billingclient.api.Purchase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingPlusViewModel @Inject constructor(
    private val billingClient: BillingClient,
    private val purchasePlusUseCase: PurchasePlusUseCase,
    private val consumePlusUseCase: ConsumePlusUseCase,
    private val verifyPlusUseCase: VerifyPlusUseCase,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _screenState = MutableStateFlow<ScreenState<BillingPlusUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            _screenState.value = runCatching {
                BillingPlusUiState(
                    productDetails = billingClient.queryProductDetails(ProductItem.plus, ProductType.INAPP),
                    purchase = verifyPlusUseCase.execute(),
                )
            }.fold(
                onSuccess = { ScreenState.Idle(it) },
                onFailure = { ScreenState.Error(R.string.error_no_data) },
            )
        }
    }

    fun purchase(activity: Activity) {
        viewModelScope.launch(ioDispatcher) {
            purchasePlusUseCase.execute(activity)
        }
    }

    fun consume(purchase: Purchase) {
        viewModelScope.launch(ioDispatcher) {
            consumePlusUseCase.execute(purchase)
        }
    }
}

@Stable
data class BillingPlusUiState(
    val productDetails: ProductDetails? = null,
    val purchase: Purchase? = null,
)
