package net.janczar.kotlinrx.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import net.janczar.kotlinrx.domain.model.HelloMessage


interface AuthRepository {

    fun sayAuthorizedHello(): Single<HelloMessage>

}