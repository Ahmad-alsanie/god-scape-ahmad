package com.godscape;import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

public class LoggingTest {
    private static final Logger log = LoggerFactory.getLogger(LoggingTest.class);

    public static void main(String[] args) {
        log.info("Logging test started.");
        log.debug("This is a debug message.");
        log.error("This is an error message.");
    }
}
