package com.pages;

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
    protected static final int WAIT_TIMEOUT_SECONDS = 10;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
    
    public abstract boolean isLoaded();
    
    protected void navigateTo(String url) {
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
        WebElement element = waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    protected void clickElement(By locator) {
        WebElement element = waitForElementToBeClickable(locator);
        element.click();
    }
    
    protected void clickElementWithJS(By locator) {
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
