package co.omise.android.charity.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import co.omise.android.charity.R
import co.omise.android.charity.util.network.NetworkState
import co.omise.android.charity.util.network.Status
import kotlinx.android.synthetic.main.list_item_network_state.view.*
import java.net.UnknownHostException

class NetworkStateViewHolder private constructor(view: View, retryCallback: () -> Unit) :
    PagedViewHolder<NetworkState>(view) {

    init {
        itemView.retryButton.setOnClickListener {
            retryCallback()
        }
    }

    override fun bindView(model: NetworkState) {

        itemView.progressBar.isVisible = model.status == Status.RUNNING
        itemView.retryButton.isVisible = model.status == Status.FAILED
        itemView.errorMessageTextView.isVisible = model.t != null

        if (model.t is UnknownHostException) {
            itemView.errorMessageTextView.text = itemView.resources.getString(R.string.offline_please_check_connection)
        } else {
            itemView.errorMessageTextView.text = model.t?.message ?: "Unknown Error"
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit) =
            NetworkStateViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_network_state,
                    parent,
                    false
                ), retryCallback
            )
    }
}