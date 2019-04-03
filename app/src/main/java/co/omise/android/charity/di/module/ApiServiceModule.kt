package co.omise.android.charity.di.module

import co.omise.android.charity.api.service.CharityService
import co.omise.android.charity.api.service.DonationService
import co.omise.android.charity.di.scope.OmiseAppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module(includes = [ApiModule::class])
class ApiServiceModule {

    @Provides
    @OmiseAppScope
    fun provideCharityService(@Named("retrofitOpenApiClient") retrofit: Retrofit) =
        retrofit.create(CharityService::class.java)

    @Provides
    @OmiseAppScope
    fun provideDonationService(@Named("retrofitOpenApiClient") retrofit: Retrofit) =
        retrofit.create(DonationService::class.java)
}