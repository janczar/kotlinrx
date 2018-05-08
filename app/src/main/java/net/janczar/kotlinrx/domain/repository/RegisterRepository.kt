package net.janczar.kotlinrx.domain.repository

import io.reactivex.Single
import net.janczar.kotlinrx.domain.model.RegisterForm
import net.janczar.kotlinrx.domain.model.RegisterResult


interface RegisterRepository {
    fun register(registerForm: RegisterForm): Single<RegisterResult>
}