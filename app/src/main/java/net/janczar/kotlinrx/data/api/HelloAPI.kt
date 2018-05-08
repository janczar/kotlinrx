package net.janczar.kotlinrx.data.api

import io.reactivex.Single
import net.janczar.kotlinrx.data.model.HelloResponse
import retrofit2.http.GET


interface HelloAPI {

    @GET("hello")
    fun sayHello(): Single<HelloResponse>

}