package com.pages;

import com.utils.TestLogger;
import com.utils.WaitActions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Clase base para todos los Page Objects.
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WaitActions waitActions; // E2: Wrapper de waits
    protected static final int WAIT_TIMEOUT_SECONDS = 10;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitActions = new WaitActions(driver); // E2: Inicializar wrapper
    }
    
    public abstract boolean isLoaded();
    
    protected void navigateTo(String url) {
        TestLogger.logNavigation(url); // E4: Log navegación
        driver.get(url);
    }
    
    protected WebElement waitForElementToBeVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    protected WebElement waitForElementToBeClickable(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void typeText(By locator, String text) {
        TestLogger.logType(locator.toString(), text); // E4: Log escritura
        WebElement element = waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    protected void clickElement(By locator) {
        TestLogger.logClick(locator.toString()); // E4: Log click
        WebElement element = waitForElementToBeClickable(locator);
        element.click();
    }
    
    protected void clickElementWithJS(By locator) {
        TestLogger.logClick(locator.toString() + " (JS)"); // E4: Log JS click
        WebElement element = waitForElementToBeClickable(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
    
    protected boolean isElementVisible(By locator) {
        try {
            waitForElementToBeVisible(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    protected String getElementText(By locator) {
        return waitForElementToBeVisible(locator).getText();
    }
    
    protected boolean waitForUrl(String expectedUrl) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS))
                    .until(ExpectedConditions.urlToBe(expectedUrl));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    protected String getPageTitle() {
        return driver.getTitle();
    }
}
