package co.omise.android.charity.repository

import co.omise.android.charity.api.service.CharityService
import co.omise.android.charity.datasource.factory.CharityDataSourceFactory
import co.omise.android.charity.model.Charity
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

class CharityListRepository @Inject constructor(
    charityService: CharityService,
    @Named("networkIO") networkExecutor: Executor, @Named("pageSize") pageSize: Int
) :
    InMemoryPageRepository<Int, Charity>(
        CharityDataSourceFactory(charityService, networkExecutor),
        pageSize,
        networkExecutor
    )
