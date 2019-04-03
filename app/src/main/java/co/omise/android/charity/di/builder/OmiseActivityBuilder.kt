package co.omise.android.charity.di.builder

import co.omise.android.charity.ui.activity.LauncherActivity
import co.omise.android.charity.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface OmiseActivityBuilder {

    @ContributesAndroidInjector
    fun bindLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity
}