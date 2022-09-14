package com.navi.networking

import com.navi.logger.Logger
import com.navi.networking.models.ErrorModel
import com.navi.utils.stringify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import kotlin.math.pow

private typealias ApiCall<X> = suspend () -> Response<X>

internal suspend fun <T> backOffCall(
    times: Int = 3,
    initialDelay: Long = 100, // 0.1 second
    maxDelay: Long = 1000,    // 1 second
    factor: Double = 2.0,
    apiCall: ApiCall<T> /* = suspend () -> retrofit2.Response<T> */
): ApiResponse<T> {
    var currentDelay = initialDelay
    repeat(times) {
        apiCall.execute().fold(
            onSuccess = { return ApiResponse.Success(it) },
            onError = { Logger.d(it) }
        )
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return apiCall.execute() // last attempt
}

private suspend fun <T> ApiCall<T>.execute(): ApiResponse<T> {
    return try {
        val response = this()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResponse.Success(it)
            } ?: throw response.inspection()
        } else {
            throw response.inspection()
        }
    } catch (t: Throwable) {
        ApiResponse.Error(t)
    }
}

private fun <T> Response<T>.inspection(): IOException {
    val message = this.errorBody()?.let {
        runCatching { it.string() }.getOrDefault(this.message())
    } ?: this.message()
    val errorModel = ErrorModel(this.code(), ErrorModel.ErrorMessageModel(message))
    return IOException(errorModel.stringify()) // currently retrying for all sorts of failures
}

@Suppress("unused")
internal fun <T> safeApiCall(
    retryPolicy: RetryPolicy = RetryPolicy.DEFAULT,
    apiCall: ApiCall<T> /* = suspend () -> retrofit2.Response<T> */
): Flow<ApiResponse<T>> = flow<ApiResponse<T>> {
    apiCall.execute().fold(
        onSuccess = { emit(ApiResponse.Success(it)) },
        onError = { throw it }
    )
}.retryWithPolicy(retryPolicy).catch {
    emit(ApiResponse.Error(it))
}.cancellable()

private fun <T> Flow<T>.retryWithPolicy(retryPolicy: RetryPolicy): Flow<T> {
    return retryWhen { cause, attempt ->
        Logger.d(cause)
        if ((cause is IOException) and (attempt < retryPolicy.retries)) {
            delay(retryPolicy.retryStrategy.calculateDelay(attempt, retryPolicy.delayMillis))
            return@retryWhen true
        } else {
            return@retryWhen false
        }
    }
}

internal data class RetryPolicy(
    val retries: Long = 3,
    val delayMillis: Long = 100L, // 0.1 second
    val retryStrategy: RetryStrategy = RetryStrategy.Exponential,
) {
    companion object {
        val DEFAULT by lazy { RetryPolicy() }
    }
}

internal sealed interface RetryStrategy {
    fun calculateDelay(attempt: Long, delayMillis: Long): Long

    @Suppress("unused")
    object Constant : RetryStrategy {
        override fun calculateDelay(attempt: Long, delayMillis: Long): Long = 0L
    }

    @Suppress("unused")
    object Linear : RetryStrategy {
        override fun calculateDelay(attempt: Long, delayMillis: Long): Long {
            return 1F.pow(attempt.toFloat()).toLong().times(delayMillis)
        }
    }

    object Exponential : RetryStrategy {
        override fun calculateDelay(attempt: Long, delayMillis: Long): Long {
            return 2F.pow(attempt.toFloat()).toLong().times(delayMillis)
        }
    }

    @Suppress("unused")
    data class Custom(private val delayFactor: Float) : RetryStrategy {
        override fun calculateDelay(attempt: Long, delayMillis: Long): Long {
            return delayFactor.pow(attempt.toFloat()).toLong().times(delayMillis)
        }
    }
}

internal sealed class ApiResponse<Y> {
    data class Success<Y>(val data: Y) : ApiResponse<Y>()
    data class Error<Y>(val throwable: Throwable) : ApiResponse<Y>()

    inline fun fold(onSuccess: (Y) -> Unit, onError: (Throwable) -> Unit) {
        return when (this) {
            is Success -> onSuccess(this.data)
            is Error -> onError(this.throwable)
        }
    }
}