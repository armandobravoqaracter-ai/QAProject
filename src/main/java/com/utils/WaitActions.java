package com.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * E2 - Wrapper centralizado de waits explícitas.
 * Elimina necesidad de Thread.sleep y estandariza esperas.
 */
public class WaitActions {
    
    private final WebDriver driver;
    private final int defaultTimeout;
    private final int defaultPolling;
    
    public WaitActions(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = Integer.parseInt(
            System.getProperty("wait.timeout", "10")
        );
        this.defaultPolling = Integer.parseInt(
            System.getProperty("wait.polling", "500")
        );
    }
    
    public WebElement waitVisible(By locator) {
        return waitVisible(locator, defaultTimeout);
    }

    public WebElement waitVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitVisible(WebElement element) {
        return waitVisible(element, defaultTimeout);
    }
    
    public WebElement waitVisible(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    public WebElement waitClickable(By locator) {
        return waitClickable(locator, defaultTimeout);
    }

    public WebElement waitClickable(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitClickable(WebElement element) {
        return waitClickable(element, defaultTimeout);
    }
    
    public WebElement waitClickable(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitUrlContains(String urlFragment) {
        return waitUrlContains(urlFragment, defaultTimeout);
    }
    
    public boolean waitUrlContains(String urlFragment, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }
    
    public boolean waitCount(By locator, int expectedCount) {
        return waitCount(locator, expectedCount, defaultTimeout);
    }
    
    public boolean waitCount(By locator, int expectedCount, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            return elements.size() == expectedCount;
        });
    }
    
    public boolean waitTextContains(By locator, String text) {
        return waitTextContains(locator, text, defaultTimeout);
    }

    public boolean waitTextContains(By locator, String text, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    public boolean waitInvisible(By locator) {
        return waitInvisible(locator, defaultTimeout);
    }

    public boolean waitInvisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    public int getDefaultTimeout() {
        return defaultTimeout;
    }
    
    public int getDefaultPolling() {
        return defaultPolling;
    }
}
