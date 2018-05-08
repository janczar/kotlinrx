package net.janczar.kotlinrx.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.janczar.kotlinrx.data.api.AuthAPI
import net.janczar.kotlinrx.data.mapNetworkErrors
import net.janczar.kotlinrx.data.mapToDomain
import net.janczar.kotlinrx.data.net.AuthInterceptor
import net.janczar.kotlinrx.data.retry
import net.janczar.kotlinrx.domain.model.HelloMessage
import net.janczar.kotlinrx.domain.repository.AuthRepository
import retrofit2.HttpException
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class RestAuthRepository(
        retrofit: Retrofit,
        private val authInterceptor: AuthInterceptor
): AuthRepository {

    companion object {
        private const val MAX_RETRIES = 5
    }

    private val authAPI = retrofit.create(AuthAPI::class.java)

    override fun sayAuthorizedHello(): Single<HelloMessage> {
        return authAPI
                .sayAuthorizedHello()
                .retry(MAX_RETRIES, {
                    error ->
                        if (error is HttpException && error.code() == 401)
                            refreshToken()
                        else
                            Flowable.error(error)
                })
                .mapNetworkErrors()
                .mapToDomain()
    }

    private fun refreshToken(): Flowable<String> {
        return Completable.fromAction {
                authInterceptor.authToken = "1234"
            }
            .delay(500, TimeUnit.MILLISECONDS)
            .toSingleDefault("")
            .toFlowable()
    }


}