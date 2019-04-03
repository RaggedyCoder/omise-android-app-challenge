package co.omise.android.charity.api.response

import android.os.Parcelable
import co.omise.android.charity.exception.ApiResponseException
import co.omise.android.charity.util.network.NetworkState

open class ErrorResult<out T : Parcelable> private constructor(val response: T) {

    class NetworkError(response: NetworkErrorResponse) : ErrorResult<NetworkErrorResponse>(response)
    class BadRequestError(response: BadRequestResponse) : ErrorResult<BadRequestResponse>(response)
    class UnknownError(response: UnknownErrorResponse) : ErrorResult<UnknownErrorResponse>(response)
    class InvalidResponseError(response: InvalidResponse) : ErrorResult<InvalidResponse>(response)

    fun toNetworkError(message: String = "Unknown Error") = NetworkState.error(ApiResponseException(this, message))
}