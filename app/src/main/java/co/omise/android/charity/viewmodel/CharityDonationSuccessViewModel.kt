package co.omise.android.charity.viewmodel

import android.app.Application
import co.omise.android.charity.di.AndroidViewModelInjection

class CharityDonationSuccessViewModel(application: Application) : OmiseBaseViewModel(application) {

    init {
        AndroidViewModelInjection.inject(this)
    }
}