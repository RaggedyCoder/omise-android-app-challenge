package co.omise.android.charity.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.omise.android.charity.datasource.NetworkDataSource

abstract class NetworkDataSourceFactory<Key, Value> : DataSource.Factory<Key, Value>() {
    val sourceLiveData = MutableLiveData<NetworkDataSource<Key, Value>>()
}