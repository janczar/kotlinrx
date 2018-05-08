package net.janczar.kotlinrx.data

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import net.janczar.kotlinrx.data.model.DomainMappable
import net.janczar.kotlinrx.domain.model.HttpCallFailureException
import net.janczar.kotlinrx.domain.model.MaxRetriesExceededException
import net.janczar.kotlinrx.domain.model.ServerUnreachableException
import net.janczar.kotlinrx.domain.model.NoNetworkException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <reified T : R, R> Single<out R>.mapError(): Single<R> =
        this.map { it as R }
                .onErrorResumeNext { error ->
                    if (error is HttpException && error.code() >= 400) {
                        mapErrorBody(error, T::class.java)?.let {
                            Single.just(it)
                        } ?: Single.error(IllegalStateException("Mapping http body failed!"))
                    } else {
                        Single.error(error)
                    }
                }

val moshi: Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

fun <T> mapErrorBody(error: HttpException, type: Class<T>) =
        error.response().errorBody()?.let {
            moshi
                    .adapter<T>(type)
                    .fromJson(
                            it.string()
                    )
        }

fun <T : DomainMappable<R>, R> Single<T>.mapToDomain(): Single<R> = this.map { it.asDomain() }

fun <T> Single<T>.mapNetworkErrors(): Single<T> =
        this.onErrorResumeNext{
            error -> when (error) {
                is SocketTimeoutException -> Single.error(NoNetworkException(error))
                is UnknownHostException -> Single.error(ServerUnreachableException(error))
                is HttpException -> Single.error(HttpCallFailureException(error))
                else -> Single.error(error)
            }
        }


fun <T,R> Single<T>.retry(count: Int, action: (Throwable) -> Flowable<R>): Single<T> =
        this.retryWhen {
            errors ->
                errors.countItems().flatMap {
                    item ->
                        if (item.second < count) {
                            action.invoke(item.first)
                        } else {
                            Flowable.error<T>(MaxRetriesExceededException(item.first))
                        }
                }
        }

fun <T> Flowable<T>.countItems(): Flowable<Pair<T, Int>> =
        Flowable.zip(
                this,
                Flowable.range(1, Int.MAX_VALUE),
                BiFunction { item, count -> Pair(item, count) }
        )