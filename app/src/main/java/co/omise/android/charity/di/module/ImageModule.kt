package co.omise.android.charity.di.module

import android.app.Application
import co.omise.android.charity.di.scope.OmiseAppScope
import co.omise.android.charity.util.glide.GlideApp
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides

@Module
class ImageModule {
    @Provides
    @OmiseAppScope
    fun provideGlideRequests(application: Application): RequestManager = GlideApp.with(application)
}