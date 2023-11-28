package com.issue.tracker.infra.logger;

import com.issue.tracker.api.logger.Log;
import com.issue.tracker.api.logger.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonLog extends Log {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CommonLog(Class<?> invokerClass, LogType logType, String message) {
        super(invokerClass, logType, message);
    }

    @Override
    public void print() {
        switch (logType) {
            case DEBUG:
                logger.debug(constructPrintString());
                break;
            case INFO:
                logger.info(constructPrintString());
                break;
            case ERROR:
                logger.error(constructPrintString());
                break;
            case TRACE:
                logger.trace(constructPrintString());
                break;
            case WARNING:
                logger.warn(constructPrintString());
                break;
        }
    }

    @Override
    public String constructString() {
        return constructPrintString();
    }

    private String constructPrintString() {
        StringBuilder printString = new StringBuilder(message);
        if (reason != null) {
            printString.append(" | reason(").append(reason).append(")");
        }
        if (stackTrace != null) {
            printString.append(" | stacktrace(").append(stackTrace).append(")");
        }
        return printString.toString();
    }
}
