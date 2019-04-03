package co.omise.android.charity.api.call

import android.os.Parcelable
import co.omise.android.charity.api.response.*
import co.omise.android.charity.util.parseAs
import com.google.gson.JsonParseException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

abstract class OmiseApiCallback<T> : Callback<T> {

    final override fun onResponse(call: Call<T>, response: Response<T>) {
        try {
            when (response.code()) {
                in 200..299 -> {
                    val responseBody = response.body() ?: return
                    success(responseBody)
                }
                400 -> {
                    val errorJson =
                        response.errorBody()?.string() ?: throw IllegalArgumentException("Null Response from server")
                    val errorResponseBody = errorJson.parseAs<BadRequestResponse>()
                    error(ErrorResult.BadRequestError(errorResponseBody))
                }
            }
        } catch (ex: Exception) {
            onFailure(call, ex)
        }
    }

    final override fun onFailure(call: Call<T>, t: Throwable) {
        when (t) {
            is UnknownHostException -> {
                val errorResponseBody =
                    ErrorResult.NetworkError(NetworkErrorResponse("Offline… Please check your connection."))
                error(errorResponseBody)
            }
            is JsonParseException -> {
                val errorResponseBody =
                    ErrorResult.InvalidResponseError(InvalidResponse("Invalid response from server."))
                error(errorResponseBody)
            }
            else -> {
                val errorResponseBody =
                    ErrorResult.UnknownError(UnknownErrorResponse(t.message ?: "Unknown Error."))
                error(errorResponseBody)
            }
        }
    }

    abstract fun success(responseBody: T)

    abstract fun <A : Parcelable> error(responseBody: ErrorResult<A>)
}