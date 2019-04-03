package co.omise.android.charity.datasource

import android.os.Parcelable
import co.omise.android.charity.api.call.OmiseApiCallback
import co.omise.android.charity.api.response.*
import co.omise.android.charity.api.response.charity.CharityListResponse
import co.omise.android.charity.api.service.CharityService
import co.omise.android.charity.exception.ApiResponseException
import co.omise.android.charity.model.Charity
import co.omise.android.charity.util.network.NetworkState
import co.omise.android.charity.util.parseAs
import com.google.gson.JsonParseException
import java.net.UnknownHostException
import java.util.concurrent.Executor

class CharityDataSource(
    private val charityService: CharityService,
    retryExecutor: Executor
) : NetworkDataSource<Int, Charity>(retryExecutor) {


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Charity>) {
        initialLoad.postValue(NetworkState.REFRESHING)
        networkState.postValue(NetworkState.REFRESHING)

        try {
            val response = charityService.getCharityList(params.requestedLoadSize + 1, 0).execute()
            when (response.code()) {
                in 200..299 -> {
                    val data = response.body()?.data ?: throw  NullPointerException("Invalid response from server.")
                    if (data.size == params.requestedLoadSize + 1) {
                        callback.onResult(data.take(params.requestedLoadSize), null, params.requestedLoadSize)
                    } else {
                        callback.onResult(data, null, null)
                    }
                    networkState.postValue(NetworkState.LOADED)
                    initialLoad.postValue(NetworkState.LOADED)
                }
                400 -> {
                    retry = {
                        loadInitial(params, callback)
                    }
                    val errorJson =
                        response.errorBody()?.string() ?: throw IllegalArgumentException("Null Response from server")
                    val errorResponseBody = errorJson.parseAs<BadRequestResponse>()
                    val error = ErrorResult.BadRequestError(errorResponseBody).toNetworkError("Bad Request")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                }
                else -> {
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error =
                        ErrorResult.UnknownError(UnknownErrorResponse("Unknown Error")).toNetworkError("Unknown Error")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                }
            }
        } catch (ex: Exception) {
            retry = {
                loadInitial(params, callback)
            }
            val errorResponseBody = when (ex) {
                is UnknownHostException ->
                    ErrorResult.NetworkError(NetworkErrorResponse("Offline… Please check your connection."))
                is JsonParseException ->
                    ErrorResult.InvalidResponseError(InvalidResponse("Invalid response from server."))
                else ->
                    ErrorResult.UnknownError(UnknownErrorResponse(ex.message ?: "Unknown Error."))
            }
            val error = NetworkState.error(ApiResponseException(errorResponseBody, ex.message ?: "Unknown Error."))
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Charity>) {
        networkState.postValue(NetworkState.LOADING)
        charityService.getCharityList(params.requestedLoadSize + 1, params.key)
            .enqueue(object : OmiseApiCallback<CharityListResponse>() {
                override fun success(responseBody: CharityListResponse) {
                    if (responseBody.data.size == params.requestedLoadSize + 1) {
                        callback.onResult(
                            responseBody.data.take(params.requestedLoadSize),
                            params.key + responseBody.data.size - 1
                        )
                    } else {
                        callback.onResult(responseBody.data, null)
                    }
                    networkState.postValue(NetworkState.LOADED)
                }

                override fun <A : Parcelable> error(responseBody: ErrorResult<A>) {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(responseBody.toNetworkError())
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Charity>) {
        // No need fetch
    }
}