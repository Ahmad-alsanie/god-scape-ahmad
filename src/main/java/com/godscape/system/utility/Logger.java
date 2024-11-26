package com.godscape.system.utility;

import com.godscape.system.enums.LogLevel;
import com.godscape.system.enums.Platforms;
import com.godscape.system.factories.PlatformFactory;

import java.time.Duration;
import java.time.Instant;

public class Logger {

    // Helper method to check if logging is enabled for the current log level
    private static boolean isLogLevelEnabled(LogLevel logLevel) {
        // You can add more sophisticated level filtering logic here if needed
        return true; // By default, all log levels are enabled.
    }

    // Handles the actual logging operation based on the platform
    private static void echo(String message, LogLevel logLevel) {
        if (!isLogLevelEnabled(logLevel)) {
            return; // Don't log if the log level is not enabled.
        }

        Platforms currentPlatform = Platforms.DREAMBOT_OSRS;
        try {
            if (currentPlatform == Platforms.DREAMBOT_OSRS || currentPlatform == Platforms.DREAMBOT_RS3) {
                org.dreambot.api.utilities.Logger.log(message); // Use DreamBot's logger
            } else {
                System.out.println(message); // Fallback to console logging
            }
        } catch (Exception e) {
            System.err.println("Logging failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Replaces `{}` placeholders with `%s` to support parameterized messages
    private static String formatMessage(String message, Object... params) {
        if (message.contains("{}")) {
            // Replace `{}` with `%s` for each occurrence, to allow for param substitution
            message = message.replace("{}", "%s");
        }
        return params.length > 0 ? String.format(message, params) : message; // Format message if params exist
    }

    // Generic logging method that also adds the caller class and method name
    private static void log(LogLevel logLevel, String message, Object... params) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace(); // Get stack trace
        String className = "UnknownClass";
        String methodName = "UnknownMethod";
        if (stackTrace.length > 3) {
            StackTraceElement callerFrame = stackTrace[3]; // Extract the caller's frame
            className = trimClassName(callerFrame.getClassName()); // Trim package name
            methodName = callerFrame.getMethodName();
        }

        String threadName = Thread.currentThread().getName(); // Get the current thread name

        String formattedMessage;
        try {
            // Format the message with params
            String userMessage = formatMessage(message, params);
            // Create the formatted log message
            formattedMessage = String.format("[%s] [%s] [%s:%s]: %s", logLevel, threadName, className, methodName, userMessage);
        } catch (Exception e) {
            // Fallback if there's a formatting issue
            formattedMessage = String.format("[%s] [%s] [FORMAT ERROR] [%s:%s]: %s | Exception: %s",
                    logLevel, threadName, className, methodName, message, e.getMessage());
        }

        echo(formattedMessage, logLevel); // Output the log message
    }

    // Trims the first two package segments from the class name
    private static String trimClassName(String fullClassName) {
        String[] parts = fullClassName.split("\\.");
        if (parts.length > 2) {
            return String.join(".", java.util.Arrays.copyOfRange(parts, 2, parts.length));
        }
        return fullClassName;
    }

    // Log info level
    public static void info(String message, Object... params) {
        log(LogLevel.INFO, message, params);
    }

    // Log debug level
    public static void debug(String message, Object... params) {
        log(LogLevel.DEBUG, message, params);
    }

    // Log warn level
    public static void warn(String message, Object... params) {
        log(LogLevel.WARN, message, params);
    }

    // Log error level
    public static void error(String message, Object... params) {
        log(LogLevel.ERROR, message, params);
    }

    // Log fatal level
    public static void fatal(String message, Object... params) {
        log(LogLevel.FATAL, message, params);
    }

    // Logs an error message with an exception
    public static void error(String message, Throwable throwable) {
        String formattedMessage = String.format("[ERROR] [%s] %s | Exception: %s", Thread.currentThread().getName(), message, throwable.getMessage());
        echo(formattedMessage, LogLevel.ERROR);
        throwable.printStackTrace();
    }

    // Logs execution time of a task
    public static void executionTime(LogLevel logLevel, String message, Runnable task) {
        Instant start = Instant.now();
        try {
            task.run();
        } finally {
            Instant end = Instant.now();
            long timeElapsed = Duration.between(start, end).toMillis();
            log(logLevel, String.format("%s | Execution Time: %d ms", message, timeElapsed));
        }
    }
}
