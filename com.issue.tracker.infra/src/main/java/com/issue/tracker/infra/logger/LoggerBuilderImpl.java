package com.issue.tracker.infra.logger;

import com.issue.tracker.api.logger.Log;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.api.logger.LoggerBuilderBase;
import jakarta.ejb.Stateless;

import java.io.Serializable;

@Stateless
public class LoggerBuilderImpl extends LoggerBuilderBase implements LoggerBuilder, Serializable {
    @Override
    public LoggerBuilderBase create(Class<?> invokerClass, LogType logType, String message) {
        this.invokerClass = invokerClass;
        this.logType = logType;
        this.message = message;
        return this;
    }

    @Override
    public Log build() {
        if (invokerClass == null) {
            throw new RuntimeException("Invoker class is not set");
        }
        CommonLog log = new CommonLog(invokerClass, logType, message);
        if (stackTrace != null) {
            log.setStackTrace(stackTrace);
        }
        if (reason != null) {
            log.setReason(reason);
        }
        return log;
    }
}
