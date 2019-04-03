package co.omise.android.charity.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import co.omise.android.charity.util.network.NetworkState

data class Listing<Model>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<Model>>,
    // represents the network request status to show to the peopleProfile
    val networkState: LiveData<NetworkState>,
    // represents the refresh status to show to the peopleProfile. Separate from networkState, this
    // value is importantly only when refresh is requested.
    val refreshState: LiveData<NetworkState>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries any failed requests.
    val retry: () -> Unit
)
