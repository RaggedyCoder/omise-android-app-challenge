package co.omise.android.charity.di.module

import co.omise.android.charity.di.scope.OmiseAppScope
import co.omise.android.charity.util.ApiConstants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module(includes = [ApiUtilityModule::class])
class ApiModule {

    @Provides
    @OmiseAppScope
    @Named("openApiHttpClient")
    fun provideopeApiHttpClient() =
        OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()

    @Provides
    @OmiseAppScope
    @Named("retrofitOpenApiClient")
    fun provideRetrofitOpenApiClient(
        @Named("openApiHttpClient") httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) =
        Retrofit.Builder().baseUrl(ApiConstants.BASE_URL_OMISE)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
}