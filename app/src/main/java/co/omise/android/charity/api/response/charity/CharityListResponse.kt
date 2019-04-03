package co.omise.android.charity.api.response.charity

import android.os.Parcelable
import co.omise.android.charity.model.Charity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharityListResponse(
    val total: Int,
    val data: List<Charity>
) : Parcelable