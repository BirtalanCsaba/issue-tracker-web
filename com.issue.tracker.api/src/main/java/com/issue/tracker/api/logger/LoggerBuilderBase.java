package com.issue.tracker.api.logger;

import java.io.Serializable;

public abstract class LoggerBuilderBase implements LoggerBuilder, Serializable {
    protected Class<?> invokerClass;
    protected LogType logType = LogType.INFO;
    protected String message = "";
    protected String stackTrace;
    protected String reason;

    public LoggerBuilderBase() {

    }

    @Override
    public LoggerBuilder create(Class<?> invokerClass, LogType logType, String message) {
        this.invokerClass = invokerClass;
        this.logType = logType;
        this.message = message;
        return this;
    }

    @Override
    public LoggerBuilder withStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    @Override
    public LoggerBuilder withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public abstract Log build();

    public Class<?> getInvokerClass() {
        return invokerClass;
    }

    public void setInvokerClass(Class<?> invokerClass) {
        this.invokerClass = invokerClass;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
