package co.omise.android.charity.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import co.omise.android.charity.datasource.factory.NetworkDataSourceFactory
import co.omise.android.charity.model.Listing
import java.util.concurrent.Executor

abstract class InMemoryPageRepository<Key, Model>(
    private val dataSourceFactory: NetworkDataSourceFactory<Key, Model>,
    pageSize: Int,
    networkExecutor: Executor
) {

    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize * 2)
        .setEnablePlaceholders(true)
        .build()
    private val livePagedList = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        .setFetchExecutor(networkExecutor).build()

    fun getPagedListing() =
        Listing(livePagedList, Transformations.switchMap(dataSourceFactory.sourceLiveData) {
            it.networkState
        }, Transformations.switchMap(dataSourceFactory.sourceLiveData) {
            it.networkState
        }, refresh = {
            dataSourceFactory.sourceLiveData.value?.invalidate()
        }, retry = {
            dataSourceFactory.sourceLiveData.value?.retryAllFailed()
        })
}