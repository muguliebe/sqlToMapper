package com.egstep.stm.base;

import org.slf4j.LoggerFactory

open class Base {
    fun <T> loggerFor(clazz: Class<T>) = LoggerFactory.getLogger(clazz)
    open val log = loggerFor(javaClass)

    companion object {
        fun <T> loggerFor(clazz: Class<T>) = LoggerFactory.getLogger(clazz)
        open val log = loggerFor(javaClass)
    }
}
