package caios.android.kanade.core.repository.di

import caios.android.kanade.core.repository.DefaultUserDataRepository
import caios.android.kanade.core.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindUserDataRepository(
        userDataRepository: DefaultUserDataRepository,
    ): UserDataRepository
}