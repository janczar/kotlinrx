package net.janczar.kotlinrx.domain.model


open class NetworkException(error: Throwable): RuntimeException(error)

class NoNetworkException(error: Throwable): NetworkException(error)

class ServerUnreachableException(error: Throwable): NetworkException(error)

class HttpCallFailureException(error: Throwable): NetworkException(error)

class MaxRetriesExceededException(error: Throwable): NetworkException(error)