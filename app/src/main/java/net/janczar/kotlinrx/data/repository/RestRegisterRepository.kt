package net.janczar.kotlinrx.data.repository

import io.reactivex.Single
import net.janczar.kotlinrx.data.api.RegisterAPI
import net.janczar.kotlinrx.data.mapError
import net.janczar.kotlinrx.data.mapNetworkErrors
import net.janczar.kotlinrx.data.mapToDomain
import net.janczar.kotlinrx.data.model.RegisterFailedResponse
import net.janczar.kotlinrx.data.model.RegisterResponse
import net.janczar.kotlinrx.data.model.asData
import net.janczar.kotlinrx.domain.model.RegisterForm
import net.janczar.kotlinrx.domain.model.RegisterResult
import net.janczar.kotlinrx.domain.repository.RegisterRepository
import retrofit2.Retrofit


class RestRegisterRepository(retrofit: Retrofit): RegisterRepository {

    private val registerAPI = retrofit.create(RegisterAPI::class.java)

    override fun register(registerForm: RegisterForm): Single<RegisterResult> {
        return registerAPI
                .register(registerForm.asData())
                .mapError<RegisterFailedResponse, RegisterResponse>()
                .mapNetworkErrors()
                .mapToDomain()
    }

}