package co.omise.android.charity.api.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DonationRequest(
    val name: String,
    val token: String,
    val amount: Int
) : Parcelable