package co.omise.android.charity.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import co.omise.android.charity.OmiseApplication
import co.omise.android.charity.model.Listing
import co.omise.android.charity.repository.InMemoryPageRepository
import co.omise.android.charity.util.isOnline

abstract class OmiseRepositoryViewModel<Key, Model>(application: Application) : OmiseBaseViewModel(application) {

    private val repository: InMemoryPageRepository<Key, Model> by lazy {
        createRepository()
    }

    val repositoryResult = MutableLiveData<Listing<Model>>()
    val pagedData = switchMap(repositoryResult) { it.pagedList }
    val networkState = switchMap(repositoryResult) { it.networkState }
    val refreshState = switchMap(repositoryResult) { it.refreshState }

    open fun fetchData(vararg arguments: Any) {
        if (getApplication<OmiseApplication>().isOnline()) {
            repositoryResult.value = repository.getPagedListing()
        } else {
            setNoInternetRetry {
                repositoryResult.value = repository.getPagedListing()
            }
        }
    }

    open fun refreshData() {
        if (getApplication<OmiseApplication>().isOnline()) {
            repositoryResult.value?.refresh!!()
        } else {
            setNoInternetRetry {
                repositoryResult.value?.refresh!!()
            }
        }
    }

    fun retry() {
        if (getApplication<OmiseApplication>().isOnline()) {
            repositoryResult.value?.retry!!()
        } else {
            setNoInternetRetry {
                repositoryResult.value?.retry!!()
            }
        }
    }

    fun getData(position: Int) =
        repositoryResult.value?.pagedList?.value!![position]

    abstract fun createRepository(): InMemoryPageRepository<Key, Model>
}