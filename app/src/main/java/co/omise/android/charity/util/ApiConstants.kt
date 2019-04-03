package co.omise.android.charity.util

import co.omise.android.charity.BuildConfig

object ApiConstants {
    const val BASE_URL_OMISE = BuildConfig.BASE_URL_OMISE

    const val API_PAGINATION_LIMIT_QUERY = "limit"
    const val API_PAGINATION_OFFSET_QUERY = "offset"

    const val API_VERSION_1_0_0 = "1.0.0/"

    const val CHARITY_LIST_ENDPOINT = "charities/"
    const val DONATION_ENDPOINT = "donations/"
}