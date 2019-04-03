package co.omise.android.charity.api.response.donation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DonationResponse(
    val success: Boolean,
    val errorMessage: String
) : Parcelable