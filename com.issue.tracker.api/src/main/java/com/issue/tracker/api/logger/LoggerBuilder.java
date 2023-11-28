package com.issue.tracker.api.logger;

import jakarta.ejb.Remote;

@Remote
public interface LoggerBuilder {
    LoggerBuilder create(Class<?> invokerClass, LogType logType, String message);

    LoggerBuilder withStackTrace(String stackTrace);

    LoggerBuilder withReason(String reason);

    Log build();
}
