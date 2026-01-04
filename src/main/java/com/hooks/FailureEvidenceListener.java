package com.hooks;

import com.utils.TestLogger;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * E4 - Listener para captura automática de evidencia en failures.
 */
public class FailureEvidenceListener {
    
    private final BaseTest baseTest;
    private WebDriver driver;
    private static final String BASE_FOLDER = "target/evidence/";
    private static String currentExecutionFolder;
    
    public FailureEvidenceListener(BaseTest baseTest) {
        this.baseTest = baseTest;
        initializeExecutionFolder();
    }
    
    private void initializeExecutionFolder() {
        if (currentExecutionFolder == null) {
            currentExecutionFolder = BASE_FOLDER + TestLogger.generateExecutionFolder() + "/";
            try {
                Files.createDirectories(Paths.get(currentExecutionFolder));
                TestLogger.logInfo("Carpeta de evidencia creada: " + currentExecutionFolder);
            } catch (IOException e) {
                TestLogger.logError("Error creando carpeta de evidencia", e);
            }
        }
    }

    @After(order = 10000)  // E4: Valor ALTO = se ejecuta DESPUÉS 
    public void captureEvidenceOnFailure(Scenario scenario) {
        if (!scenario.isFailed()) {
            return;
        }
        
        try {
            this.driver = BaseTest.driver;
            
            if (this.driver == null) {
                TestLogger.logError("No se puede capturar evidencia: driver es null", null);
                return;
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_");
            
            TestLogger.logError("Test FAILED: " + scenario.getName(), null);
            TestLogger.logInfo("Capturando evidencia de falla...");
            
            String screenshotPath = captureFailureScreenshot(scenarioName, timestamp);

            String browserInfo = captureBrowserInfo(scenarioName, timestamp);
            
            if (screenshotPath != null) {
                try {
                    byte[] screenshot = Files.readAllBytes(Paths.get(screenshotPath));
                    scenario.attach(screenshot, "image/png", "Failure Screenshot");
                    TestLogger.logScreenshot(screenshotPath);
                } catch (IOException e) {
                    TestLogger.logError("Error adjuntando screenshot", e);
                }
            }
            
            TestLogger.logStepFailure(
                scenario.getName(),
                "Test failed - Evidence captured at: " + currentExecutionFolder
            );
            
            printFailureSummary(scenario, screenshotPath, browserInfo);
            
        } catch (Exception e) {
            TestLogger.logError("Error capturando evidencia de falla", e);
        }
    }
    
    private String captureFailureScreenshot(String scenarioName, String timestamp) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            String fileName = String.format("FAILURE_%s_%s.png", scenarioName, timestamp);
            String fullPath = currentExecutionFolder + fileName;
            
            Files.copy(source.toPath(), Paths.get(fullPath));
            return fullPath;
        } catch (Exception e) {
            TestLogger.logError("Error capturando screenshot de falla", e);
            return null;
        }
    }
    
    private String captureBrowserInfo(String scenarioName, String timestamp) {
        try {
            StringBuilder info = new StringBuilder();
            info.append("Scenario: ").append(scenarioName).append("\n");
            info.append("Timestamp: ").append(timestamp).append("\n");
            info.append("Current URL: ").append(driver.getCurrentUrl()).append("\n");
            info.append("Browser: ").append(System.getProperty("SELENIUM_GRID_BROWSER", "chrome")).append("\n");
            info.append("Environment: ").append(System.getProperty("test.environment", "LOCAL")).append("\n");
            info.append("Build: ").append(System.getProperty("build.number", "LOCAL")).append("\n");
  
            String fileName = String.format("FAILURE_%s_%s.txt", scenarioName, timestamp);
            String fullPath = currentExecutionFolder + fileName;
            
            Files.write(Paths.get(fullPath), info.toString().getBytes());
            return fullPath;
        } catch (Exception e) {
            TestLogger.logError("Error guardando información del browser", e);
            return null;
        }
    }
    
    private void printFailureSummary(Scenario scenario, String screenshotPath, String infoPath) {
       System.err.println("Test: " + scenario.getName());
        System.err.println("Status: FAILED");
        System.err.println("Tags: " + scenario.getSourceTagNames());
        System.err.println("\n📁 Evidence Location:");
        System.err.println("   Folder: " + currentExecutionFolder);
        if (screenshotPath != null) {
            System.err.println("   Screenshot: " + new File(screenshotPath).getName());
        }
        if (infoPath != null) {
            System.err.println("   Context: " + new File(infoPath).getName());
        }
        System.err.println("\n💡 Diagnostic Tip:");
        System.err.println("   1. Check screenshot for visual state");
        System.err.println("   2. Review context file for URL/environment");
        System.err.println("   3. Check logs above for last action before failure");
    }

    public static String getCurrentExecutionFolder() {
        return currentExecutionFolder;
    }
}
