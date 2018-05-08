package net.janczar.kotlinrx.domain.model


data class RegisterForm(
        val name: String,
        val email: String,
        val password: String
)

interface RegisterResult

data class RegisterSuccess(
        val userId: String,
        val accessToken: String,
        val refreshToken: String
): RegisterResult

data class RegisterFailure(
        val nameError: String?,
        val emailError: String?,
        val passwordError: String?
): RegisterResult