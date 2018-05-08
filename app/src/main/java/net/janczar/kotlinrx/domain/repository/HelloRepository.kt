package net.janczar.kotlinrx.domain.repository

import io.reactivex.Single
import net.janczar.kotlinrx.domain.model.HelloMessage


interface HelloRepository {
    fun sayHello(): Single<HelloMessage>
}