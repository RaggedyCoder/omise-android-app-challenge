package co.omise.android.charity.api.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BadRequestResponse(
    val success: Boolean,
    val errorCode: String,
    val errorMessage: String
) : Parcelable