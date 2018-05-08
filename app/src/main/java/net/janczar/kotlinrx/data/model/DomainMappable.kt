package net.janczar.kotlinrx.data.model


interface DomainMappable<R> {
    fun asDomain(): R
}