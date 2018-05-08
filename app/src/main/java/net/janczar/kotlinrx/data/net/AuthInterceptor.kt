package net.janczar.kotlinrx.data.net

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor : Interceptor {

    var authToken = ""

    override fun intercept(chain: Interceptor.Chain?): Response {
        if (chain != null) {
            return chain.proceed(
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Auth-Token", authToken)
                        .build()
            )
        } else {
            throw IllegalStateException()
        }
    }
}