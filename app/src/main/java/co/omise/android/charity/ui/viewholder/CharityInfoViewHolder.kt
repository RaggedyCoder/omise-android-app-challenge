package co.omise.android.charity.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import co.omise.android.charity.R
import co.omise.android.charity.model.Charity
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.list_item_charity.view.*

class CharityInfoViewHolder private constructor(
    private val glideRequests: RequestManager,
    itemView: View,
    onClickAction: ((itemPosition: Int) -> Unit)
) :
    PagedViewHolder<Charity>(itemView) {

    init {
        itemView.setOnClickListener {
            onClickAction(adapterPosition)
        }
    }

    override fun bindView(model: Charity) {
        val drawable = VectorDrawableCompat.create(
            itemView.resources, R.drawable.ic_account_24dp,
            itemView.context.theme
        )

        glideRequests.load(model.logoUrl)
            .placeholder(drawable)
            .error(drawable)
            .transform(CircleCrop()).into(itemView.charityIconImageView)

        itemView.charityNameTextView.text = model.name
    }

    companion object {
        fun create(parent: ViewGroup, glideRequests: RequestManager, onClickAction: ((itemPosition: Int) -> Unit)) =
            CharityInfoViewHolder(
                glideRequests,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_charity,
                    parent,
                    false
                ), onClickAction
            )
    }
}