package caios.android.kanade.core.billing.di

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
    fun bindBillingRepository(billingClientProvider: BillingClientProviderImpl): BillingClientProvider
}
