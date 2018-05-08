package net.janczar.kotlinrx.data.model

import net.janczar.kotlinrx.domain.model.HelloMessage


data class HelloResponse(
        val message: String
): DomainMappable<HelloMessage> {
    override fun asDomain() = HelloMessage(message)
}