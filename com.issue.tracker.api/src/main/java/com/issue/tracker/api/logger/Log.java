package com.issue.tracker.api.logger;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Log implements LogOperations, Serializable {
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

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setInvokerClass(Class<?> invokerClass) {
        this.invokerClass = invokerClass;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}