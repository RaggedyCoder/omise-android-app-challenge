package co.omise.android.charity.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import co.omise.android.charity.R
import co.omise.android.charity.ui.adapter.CharityInfoListAdapter
import co.omise.android.charity.util.network.NetworkState
import co.omise.android.charity.viewmodel.CharityListViewModel
import com.bumptech.glide.RequestManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_charity_list.*
import javax.inject.Inject

class CharityListFragment : OmiseBaseFragment() {

    @Inject
    lateinit var glideRequests: RequestManager

    private lateinit var charityInfoListAdapter: CharityInfoListAdapter

    override fun createViewModel() = ViewModelProviders.of(this)[CharityListViewModel::class.java]

    private val charityListViewModel by lazy {
        getViewModel() as CharityListViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_charity_list, container, false)!!

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeRefreshLayout()
        charityListRecyclerView.adapter = charityInfoListAdapter
        charityListViewModel.fetchData()
    }

    private fun initAdapter() {
        charityInfoListAdapter = CharityInfoListAdapter(glideRequests, {
            charityListViewModel.retry()
        }) {
            val charity = charityListViewModel.getData(it) ?: return@CharityInfoListAdapter
            view?.findNavController()
                ?.navigate(CharityListFragmentDirections.actionGoToCharityDonationFragment(charity))
        }

        charityListViewModel.pagedData.observe(this, Observer {
            charityListRecyclerView.isVisible = !it.isNullOrEmpty()
            noDataMessageTextView.isVisible = it.isNullOrEmpty()
            charityInfoListAdapter.submitList(it)
        })

        charityListViewModel.networkState.observe(this, Observer {
            charityInfoListAdapter.setNetworkState(it)
        })
    }

    private fun initSwipeRefreshLayout() {
        charityListViewModel.refreshState.observe(this, Observer {
            charityListRefreshLayout.isRefreshing = it == NetworkState.REFRESHING
        })

        charityListRefreshLayout.setOnRefreshListener {
            charityListViewModel.refreshData()
        }
    }
}