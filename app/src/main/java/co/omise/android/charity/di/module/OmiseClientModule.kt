package co.omise.android.charity.di.module

import co.omise.android.Client
import co.omise.android.charity.BuildConfig
import co.omise.android.charity.di.scope.OmiseAppScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class OmiseClientModule {

    @Provides
    @OmiseAppScope
    @Named("omisePublicKey")
    fun provideOmisePublicKey() = BuildConfig.PUBLIC_KEY_OMISE

    @Provides
    @OmiseAppScope
    fun provideOmiseClient(@Named("omisePublicKey") omisePublicKey: String) = Client(omisePublicKey)
}