package co.omise.android.charity.di.module

import co.omise.android.charity.di.scope.OmiseAppScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named

@Module
class RepositoryModule {
    @Provides
    @OmiseAppScope
    @Named("pageSize")
    fun providePageSize() = 10

    @Provides
    @OmiseAppScope
    @Named("networkIO")
    fun provideNetworkIO(): Executor = Executors.newFixedThreadPool(5)

    @Provides
    @OmiseAppScope
    @Named("diskIO")
    fun provideDiskIO(): Executor = Executors.newSingleThreadExecutor()
}