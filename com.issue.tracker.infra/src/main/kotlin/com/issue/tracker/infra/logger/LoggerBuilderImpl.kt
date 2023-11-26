package com.issue.tracker.infra.logger

import com.issue.tracker.api.logger.Log
import com.issue.tracker.api.logger.LogType
import com.issue.tracker.api.logger.LoggerBuilder
import jakarta.ejb.Local
import jakarta.ejb.Singleton
import kotlin.reflect.KClass

@Singleton
@Local
open class LoggerBuilderImpl : LoggerBuilder() {
    override fun create(
        invokerClass: KClass<*>,
        logType: LogType,
        message: String
    ): LoggerBuilder {
        this.invokerClass = invokerClass
        this.logType = logType
        this.message = message
        return this
    }

    override fun build(): Log {
        if (invokerClass == null) {
            throw Exception()
        }
        val log = CommonLog(invokerClass!!, logType, message)
        if (stackTrace != null) {
            log.stackTrace = stackTrace
        }
        if (reason != null) {
            log.reason = reason
        }
        return log
    }
}