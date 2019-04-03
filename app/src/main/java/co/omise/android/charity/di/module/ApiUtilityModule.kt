package co.omise.android.charity.di.module

import co.omise.android.charity.di.scope.OmiseAppScope
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class ApiUtilityModule {

    @Provides
    @OmiseAppScope
    @Named("apiSerializerGson")
    fun provideApiSerializerGson() =
        GsonBuilder()
            .enableComplexMapKeySerialization()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting().create()!!

    @Provides
    @OmiseAppScope
    fun provideApiBodyGsonConverterFactory(@Named("apiSerializerGson") gson: Gson) =
        GsonConverterFactory.create(gson)
}