package co.omise.android.charity.di.builder

import co.omise.android.charity.viewmodel.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface OmiseViewModelBuilder {
    @ContributesAndroidInjector
    fun bindLauncherViewModel(): LauncherViewModel

    @ContributesAndroidInjector
    fun bindCharityListViewModel(): CharityListViewModel

    @ContributesAndroidInjector
    fun bindMainViewModel(): MainViewModel

    @ContributesAndroidInjector
    fun bindCharityDonationViewModel(): CharityDonationViewModel

    @ContributesAndroidInjector
    fun bindCharityDonationSuccessViewModel(): CharityDonationSuccessViewModel
}