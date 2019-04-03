package co.omise.android.charity.exception

import co.omise.android.charity.api.response.ErrorResult
import java.io.IOException

class ApiResponseException(
    val error: ErrorResult<*>,
    message: String
) : IOException(message)