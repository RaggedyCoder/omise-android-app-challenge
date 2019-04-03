package co.omise.android.charity.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class PagedViewHolder<in Model>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(model: Model)

}