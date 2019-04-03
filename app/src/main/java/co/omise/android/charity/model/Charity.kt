package co.omise.android.charity.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Charity(
    val id: Int,
    val name: String,
    val logoUrl: String
) : Parcelable