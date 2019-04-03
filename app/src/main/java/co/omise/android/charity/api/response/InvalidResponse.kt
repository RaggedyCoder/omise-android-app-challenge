package co.omise.android.charity.api.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvalidResponse(
    val message: String
) : Parcelable