package com.issue.tracker.api.logger;

public abstract class LoggerBuilder {
    protected Class<?> invokerClass;
    protected LogType logType = LogType.INFO;
    protected String message = "";
    protected String stackTrace;
    protected String reason;

    protected LoggerBuilder create(Class<?> invokerClass, LogType logType, String message) {
        this.invokerClass = invokerClass;
        this.logType = logType;
        this.message = message;
        return this;
    }

    protected LoggerBuilder withStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    protected LoggerBuilder withReason(String reason) {
        this.reason = reason;
        return this;
    }

    protected abstract Log build();
}
