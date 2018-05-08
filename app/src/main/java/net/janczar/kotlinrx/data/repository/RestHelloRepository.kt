package net.janczar.kotlinrx.data.repository

import io.reactivex.Single
import net.janczar.kotlinrx.data.api.HelloAPI
import net.janczar.kotlinrx.data.mapNetworkErrors
import net.janczar.kotlinrx.data.mapToDomain
import net.janczar.kotlinrx.domain.model.HelloMessage
import net.janczar.kotlinrx.domain.repository.HelloRepository
import retrofit2.Retrofit

class RestHelloRepository(
        retrofit: Retrofit
): HelloRepository {

    private val helloAPI = retrofit.create(HelloAPI::class.java)

    override fun sayHello(): Single<HelloMessage> =
            helloAPI
                    .sayHello()
                    .mapNetworkErrors()
                    .mapToDomain()
}