package co.omise.android.charity.api.service

import co.omise.android.charity.api.response.charity.CharityListResponse
import co.omise.android.charity.util.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CharityService {

    @GET(ApiConstants.API_VERSION_1_0_0 + ApiConstants.CHARITY_LIST_ENDPOINT)
    fun getCharityList(
        @Query(ApiConstants.API_PAGINATION_LIMIT_QUERY) limit: Int,
        @Query(ApiConstants.API_PAGINATION_OFFSET_QUERY) offset: Int
    ): Call<CharityListResponse>
}