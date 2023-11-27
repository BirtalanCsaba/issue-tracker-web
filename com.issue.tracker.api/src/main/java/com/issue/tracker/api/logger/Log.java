package com.issue.tracker.api.logger;

import java.time.LocalDateTime;

public abstract class Log implements LogOperations {
    protected LogType logType;
    protected String message;
    protected Class<?> invokerClass;
    protected LocalDateTime timestamp;
    protected String stackTrace;
    protected String reason;

    public Log(Class<?> invokerClass, LogType logType, String message) {
        this.invokerClass = invokerClass;
        this.logType = logType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    protected void setLogType(LogType logType) {
        this.logType = logType;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected void setInvokerClass(Class<?> invokerClass) {
        this.invokerClass = invokerClass;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}