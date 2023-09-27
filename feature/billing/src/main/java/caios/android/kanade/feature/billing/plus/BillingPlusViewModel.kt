package caios.android.kanade.feature.billing.plus

import android.app.Activity
import android.content.Context
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
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.repository.UserDataRepository
import com.android.billingclient.api.Purchase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
class BillingPlusViewModel @Inject constructor(
    private val billingClient: BillingClient,
    private val purchasePlusUseCase: PurchasePlusUseCase,
    private val consumePlusUseCase: ConsumePlusUseCase,
    private val verifyPlusUseCase: VerifyPlusUseCase,
    private val userDataRepository: UserDataRepository,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _screenState = MutableStateFlow<ScreenState<BillingPlusUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            _screenState.value = runCatching {
                val userData = userDataRepository.userData.firstOrNull()

                BillingPlusUiState(
                    isPlusMode = userData?.isPlusMode ?: false,
                    isDeveloperMode = userData?.isDeveloperMode ?: false,
                    productDetails = billingClient.queryProductDetails(ProductItem.plus, ProductType.INAPP),
                    purchase = runCatching { verifyPlusUseCase.execute() }.getOrNull(),
                )
            }.fold(
                onSuccess = { ScreenState.Idle(it) },
                onFailure = {
                    Timber.w(it)
                    ScreenState.Error(
                        message = R.string.error_billing,
                        retryTitle = R.string.common_close,
                    )
                },
            )
        }
    }

    suspend fun purchase(activity: Activity): Boolean {
        return runCatching {
            withContext(ioDispatcher) {
                purchasePlusUseCase.execute(activity)
            }
        }.fold(
            onSuccess = {
                userDataRepository.setPlusMode(true)
                ToastUtil.show(activity, R.string.billing_plus_toast_purchased)
                true
            },
            onFailure = {
                Timber.w(it)
                ToastUtil.show(activity, R.string.billing_plus_toast_purchased_error)
                false
            },
        )
    }

    suspend fun verify(context: Context): Boolean {
        return runCatching {
            withContext(ioDispatcher) {
                verifyPlusUseCase.execute()
            }
        }.fold(
            onSuccess = {
                if (it != null) {
                    userDataRepository.setPlusMode(true)
                    ToastUtil.show(context, R.string.billing_plus_toast_verify)
                    true
                } else {
                    ToastUtil.show(context, R.string.billing_plus_toast_verify_error)
                    false
                }
            },
            onFailure = {
                Timber.w(it)
                ToastUtil.show(context, R.string.error_billing)
                false
            },
        )
    }

    suspend fun consume(context: Context, purchase: Purchase): Boolean {
        return runCatching {
            withContext(ioDispatcher) {
                consumePlusUseCase.execute(purchase)
            }
        }.fold(
            onSuccess = {
                userDataRepository.setPlusMode(false)
                ToastUtil.show(context, R.string.billing_plus_toast_consumed)
                true
            },
            onFailure = {
                Timber.w(it)
                ToastUtil.show(context, R.string.billing_plus_toast_consumed_error)
                false
            },
        )
    }
}

@Stable
data class BillingPlusUiState(
    val isPlusMode: Boolean = false,
    val isDeveloperMode: Boolean = false,
    val productDetails: ProductDetails? = null,
    val purchase: Purchase? = null,
)
