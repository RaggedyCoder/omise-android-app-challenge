package co.omise.android.charity.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import co.omise.android.charity.R
import co.omise.android.charity.model.Charity
import co.omise.android.charity.ui.viewholder.CharityInfoViewHolder
import co.omise.android.charity.ui.viewholder.NetworkStateViewHolder
import co.omise.android.charity.ui.viewholder.PagedViewHolder
import co.omise.android.charity.util.network.NetworkState
import com.bumptech.glide.RequestManager

class CharityInfoListAdapter(
    private val glideRequests: RequestManager,
    private val retryCallback: () -> Unit,
    private val onClickAction: ((position: Int) -> Unit)
) : PagedListAdapter<Charity, PagedViewHolder<*>>(CHARITY_INFO_DIFF_CALLBACK) {

    companion object {
        private val CHARITY_INFO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Charity>() {
            override fun areItemsTheSame(oldItem: Charity, newItem: Charity) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Charity, newItem: Charity) = oldItem == newItem
        }
    }

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            R.layout.list_item_charity ->
                CharityInfoViewHolder.create(parent, glideRequests, onClickAction)
            R.layout.list_item_network_state ->
                NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

    override fun onBindViewHolder(holder: PagedViewHolder<*>, position: Int) {
        when (holder) {
            is CharityInfoViewHolder -> {
                val model = getItem(position) ?: return
                holder.bindView(model)
            }
            is NetworkStateViewHolder -> {
                val model = networkState ?: return
                holder.bindView(model)
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int) =
        if (hasExtraRow() && position == itemCount - 1) {
            R.layout.list_item_network_state
        } else {
            R.layout.list_item_charity
        }

    private fun hasExtraRow() =
        networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.REFRESHING

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0

}