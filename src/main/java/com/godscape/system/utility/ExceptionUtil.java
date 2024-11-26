package com.godscape.system.utility;

import com.godscape.system.utility.Logger; // Import your custom logger

public class ExceptionUtil {

    // Static initializer for ExceptionUtil class
    static {
        Logger.info("ExceptionUtil: Initialization complete."); // Log initialization
    }

    // Re-throws a given Throwable as a RuntimeException, preserving the original exception stack trace
    public static RuntimeException rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            Logger.error("ExceptionUtil: Re-throwing RuntimeException - {}", throwable.getMessage());
            throw (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            Logger.error("ExceptionUtil: Re-throwing Error - {}", throwable.getMessage());
            throw (Error) throwable;
        } else {
            Logger.error("ExceptionUtil: Re-throwing Throwable as RuntimeException - {}", throwable.getMessage());
            throw new RuntimeException(throwable);
        }
    }

    // Logs and swallows exceptions without throwing them, allowing graceful fallback
    public static void handle(Throwable throwable) {
        Logger.error("ExceptionUtil: Handled exception - {}", throwable.getMessage());
    }
}
