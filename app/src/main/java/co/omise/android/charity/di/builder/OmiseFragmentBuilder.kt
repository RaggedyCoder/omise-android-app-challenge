package co.omise.android.charity.di.builder

import co.omise.android.charity.ui.fragment.CharityDonationFragment
import co.omise.android.charity.ui.fragment.CharityDonationSuccessFragment
import co.omise.android.charity.ui.fragment.CharityListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface OmiseFragmentBuilder {

    @ContributesAndroidInjector
    fun bindCharityListFragment(): CharityListFragment

    @ContributesAndroidInjector
    fun bindCharityDonationFragment(): CharityDonationFragment

    @ContributesAndroidInjector
    fun bindCharityDonationSuccessFragment(): CharityDonationSuccessFragment
}