package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * E4 - Logger centralizado para acciones de testing.
 */
public class TestLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private static String environment = System.getProperty("test.environment", "LOCAL");
    private static String browser = System.getProperty("SELENIUM_GRID_BROWSER", "chrome");
    private static String buildNumber = System.getProperty("build.number", "LOCAL");
    private static String dataset = "";
    
    public static void logTestMetadata(String testName, String datasetName) {
        dataset = datasetName;
        StringBuilder metadata = new StringBuilder();
        metadata.append("🧪 Test: ").append(testName).append("\n");
        metadata.append("📊 Dataset: ").append(datasetName).append("\n");
        metadata.append("🌍 Environment: ").append(environment).append("\n");
        metadata.append("🌐 Browser: ").append(browser.toUpperCase()).append("\n");
        metadata.append("🔨 Build: ").append(buildNumber).append("\n");
        metadata.append("⏰ Timestamp: ").append(getCurrentTimestamp()).append("\n");
        
        logger.info(metadata.toString());
        System.out.println(metadata.toString());
    }
    
    public static void logNavigation(String url) {
        String msg = String.format("[%s]  NAVIGATE → %s", getCurrentTimestamp(), url);
        logger.info(msg);
        System.out.println(msg);
    }
    
    public static void logClick(String elementDescription) {
        String msg = String.format("[%s] 👆 CLICK → %s", getCurrentTimestamp(), elementDescription);
        logger.info(msg);
        System.out.println(msg);
    }
    
    public static void logType(String elementDescription, String value) {
        String msg = String.format("[%s] ¡  TYPE → %s = '%s'", getCurrentTimestamp(), elementDescription, value);
        logger.info(msg);
        System.out.println(msg);
    }
    
    public static void logValidation(String validationType, String expected, String actual) {
        String msg = String.format("[%s] VALIDATE → %s | Expected: %s | Actual: %s", 
                getCurrentTimestamp(), validationType, expected, actual);
        logger.info(msg);
        System.out.println(msg);
    }
    
    public static void logWait(String condition, int timeoutSeconds) {
        String msg = String.format("[%s]  WAIT → %s (timeout: %ds)", 
                getCurrentTimestamp(), condition, timeoutSeconds);
        logger.info(msg);
        System.out.println(msg);
    }

    public static void logError(String errorDescription, Throwable exception) {
        String msg = String.format("[%s]  ERROR → %s", getCurrentTimestamp(), errorDescription);
        logger.error(msg, exception);
        System.err.println(msg);
        if (exception != null) {
            System.err.println("   Exception: " + exception.getMessage());
        }
    }
    
    public static void logScreenshot(String screenshotPath) {
        String msg = String.format("[%s]  SCREENSHOT → %s", getCurrentTimestamp(), screenshotPath);
        logger.info(msg);
        System.out.println(msg);
    }

    public static void logStepSuccess(String stepDescription) {
        String msg = String.format("[%s]  PASS → %s", getCurrentTimestamp(), stepDescription);
        logger.info(msg);
        System.out.println(msg);
    }
    
    public static void logStepFailure(String stepDescription, String reason) {
        String msg = String.format("[%s]  FAIL → %s | Reason: %s", 
                getCurrentTimestamp(), stepDescription, reason);
        logger.error(msg);
        System.err.println(msg);
    }
    
    public static void logInfo(String message) {
        String msg = String.format("[%s]  INFO → %s", getCurrentTimestamp(), message);
        logger.info(msg);
        System.out.println(msg);
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
    
    public static String generateFileName(String testName, String datasetName, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String sanitizedTest = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
        String sanitizedDataset = datasetName.replaceAll("[^a-zA-Z0-9-_]", "_");
        return String.format("%s_%s_%s.%s", sanitizedTest, sanitizedDataset, timestamp, extension);
    }
    
    public static String generateExecutionFolder() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("execution_%s_%s_%s", environment, browser, timestamp);
    }
}
