package co.omise.android.charity.viewmodel

import android.app.Application
import co.omise.android.charity.di.AndroidViewModelInjection
import co.omise.android.charity.model.Charity
import co.omise.android.charity.repository.CharityListRepository
import javax.inject.Inject

class CharityListViewModel(application: Application) : OmiseRepositoryViewModel<Int, Charity>(application) {

    @Inject
    lateinit var charityListRepository: CharityListRepository

    override fun createRepository() = charityListRepository

    init {
        AndroidViewModelInjection.inject(this)
    }
}