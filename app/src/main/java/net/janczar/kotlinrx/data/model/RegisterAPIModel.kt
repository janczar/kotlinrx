package net.janczar.kotlinrx.data.model

import net.janczar.kotlinrx.domain.model.*


data class RegisterRequest(
        val name: String,
        val email: String,
        val password: String
)

fun RegisterForm.asData() = RegisterRequest(this.name, this.email, this.password)

interface RegisterResponse: DomainMappable<RegisterResult> {
    override fun asDomain(): RegisterResult
}

data class RegisterSuccessResponse(
        private val userId: String,
        private val accessToken: String,
        private val refreshToken: String
): RegisterResponse {
    override fun asDomain() = RegisterSuccess(userId, accessToken, refreshToken)
}

data class RegisterFailedResponse(
        private val nameError: String?,
        private val emailError: String?,
        private val passwordError: String?
): RegisterResponse {
    override fun asDomain() = RegisterFailure(nameError, emailError, passwordError)
}
