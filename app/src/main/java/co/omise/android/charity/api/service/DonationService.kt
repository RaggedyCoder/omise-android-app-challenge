package co.omise.android.charity.api.service

import co.omise.android.charity.api.request.DonationRequest
import co.omise.android.charity.api.response.donation.DonationResponse
import co.omise.android.charity.util.ApiConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DonationService {

    @POST(ApiConstants.API_VERSION_1_0_0 + ApiConstants.DONATION_ENDPOINT)
    fun makeDonationRequest(
        @Body donationRequest: DonationRequest
    ): Call<DonationResponse>
}