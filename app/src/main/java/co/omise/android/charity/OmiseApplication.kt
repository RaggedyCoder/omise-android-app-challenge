package co.omise.android.charity

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.multidex.MultiDexApplication
import co.omise.android.charity.di.HasViewModelInjector
import co.omise.android.charity.di.component.DaggerOmiseApplicationComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class OmiseApplication : MultiDexApplication(), HasActivityInjector, HasViewModelInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingAndroidViewModelInjector: DispatchingAndroidInjector<AndroidViewModel>

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun androidViewModelInjector() = dispatchingAndroidViewModelInjector

    override fun supportFragmentInjector() = dispatchingFragmentInjector

    override fun activityInjector() = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()
        DaggerOmiseApplicationComponent
            .builder()
            .application(this)
            .build()
            .injectMembers(this)
    }
}