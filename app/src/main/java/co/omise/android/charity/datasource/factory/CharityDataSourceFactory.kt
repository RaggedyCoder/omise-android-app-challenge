package co.omise.android.charity.datasource.factory

import co.omise.android.charity.api.service.CharityService
import co.omise.android.charity.datasource.CharityDataSource
import co.omise.android.charity.model.Charity
import java.util.concurrent.Executor

class CharityDataSourceFactory(private val charityService: CharityService, private val retryExecutor: Executor) :
    NetworkDataSourceFactory<Int, Charity>() {
    override fun create() = CharityDataSource(charityService, retryExecutor).apply {
        sourceLiveData.postValue(this)
    }
}