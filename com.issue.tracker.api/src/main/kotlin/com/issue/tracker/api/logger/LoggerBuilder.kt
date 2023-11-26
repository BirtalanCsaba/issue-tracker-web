package com.issue.tracker.api.logger

import kotlin.reflect.KClass

abstract class LoggerBuilder {
    var invokerClass: KClass<*>? = null
        protected set

    var logType: LogType = LogType.INFO
        protected set

    var message: String = ""
        protected set

    var stackTrace: String? = null
        protected set

    var reason: String? = null
        protected set

    abstract fun create(invokerClass: KClass<*>, logType: LogType, message: String): LoggerBuilder

    fun withStackTrace(stackTrace: String?): LoggerBuilder {
        this.stackTrace = stackTrace
        return this
    }

    fun withReason(reason: String?): LoggerBuilder {
        this.reason = reason
        return this
    }

    abstract fun build(): Log
}