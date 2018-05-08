package net.janczar.kotlinrx.data.api

import io.reactivex.Single
import net.janczar.kotlinrx.data.model.HelloResponse
import retrofit2.http.GET
import retrofit2.http.Header


interface AuthAPI {

    @GET("authorized")
    fun sayAuthorizedHello(): Single<HelloResponse>

}