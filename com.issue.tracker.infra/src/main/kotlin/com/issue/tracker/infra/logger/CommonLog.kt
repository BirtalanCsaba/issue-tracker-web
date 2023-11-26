package com.issue.tracker.infra.logger

import com.issue.tracker.api.logger.Log
import com.issue.tracker.api.logger.LogType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class CommonLog(
    invokerClass: KClass<*>,
    logType: LogType,
    message: String
) : Log(invokerClass, logType, message) {
    var logger: Logger = LoggerFactory.getLogger(invokerClass.java)

    override fun print() {
        when (logType) {
            LogType.DEBUG -> logger.debug(this.constructPrintString())
            LogType.INFO -> logger.info(this.constructPrintString())
            LogType.ERROR -> logger.error(this.constructPrintString())
            LogType.TRACE -> logger.trace(this.constructPrintString())
            LogType.WARNING -> logger.warn(this.constructPrintString())
        }
    }

    override fun constructString(): String {
        return constructPrintString()
    }

    private fun constructPrintString(): String {
        var printString = message
        if (reason != null) {
            printString += " | reason($reason)"
        }
        if (stackTrace != null) {
            printString += " | stacktrace($stackTrace)"
        }
        return printString
    }

}