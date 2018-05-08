package net.janczar.kotlinrx.data.api

import io.reactivex.Single
import net.janczar.kotlinrx.data.model.RegisterRequest
import net.janczar.kotlinrx.data.model.RegisterSuccessResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface RegisterAPI {
    @POST("register")
    fun register(@Body request: RegisterRequest): Single<RegisterSuccessResponse>
}