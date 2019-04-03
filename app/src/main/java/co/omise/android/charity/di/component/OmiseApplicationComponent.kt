package co.omise.android.charity.di.component

import android.app.Application
import co.omise.android.charity.OmiseApplication
import co.omise.android.charity.di.builder.OmiseActivityBuilder
import co.omise.android.charity.di.builder.OmiseFragmentBuilder
import co.omise.android.charity.di.builder.OmiseViewModelBuilder
import co.omise.android.charity.di.module.*
import co.omise.android.charity.di.scope.OmiseAppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        OmiseActivityBuilder::class,
        OmiseFragmentBuilder::class,
        OmiseViewModelBuilder::class,
        OmiseClientModule::class,
        ApiModule::class,
        ApiUtilityModule::class,
        ApiServiceModule::class,
        RepositoryModule::class,
        ImageModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)

@OmiseAppScope
abstract class OmiseApplicationComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): OmiseApplicationComponent
    }

    abstract fun injectMembers(omiseApplication: OmiseApplication)
}