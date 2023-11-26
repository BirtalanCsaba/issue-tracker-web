package com.issue.tracker.api.logger

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KClass

abstract class Log(
    invokerClass: KClass<*>,
    logType: LogType,
    message: String,
) : LogOperations {
    var logType: LogType = logType
        protected set
    var message: String = message
        protected set

    var invokerClass: KClass<*> = invokerClass
        protected set

    val timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    var stackTrace: String? = null

    var reason: String? = null
}