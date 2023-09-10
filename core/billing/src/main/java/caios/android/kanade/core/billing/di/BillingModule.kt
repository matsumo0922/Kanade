package caios.android.kanade.core.billing.di

import caios.android.kanade.core.billing.BillingClient
import caios.android.kanade.core.billing.BillingClientImpl
import caios.android.kanade.core.billing.BillingClientProvider
import caios.android.kanade.core.billing.BillingClientProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BillingModule {

    @Singleton
    @Binds
    fun bindBillingClientProvider(billingClientProvider: BillingClientProviderImpl): BillingClientProvider

    @Singleton
    @Binds
    fun bindBillingClient(billingClient: BillingClientImpl): BillingClient
}
