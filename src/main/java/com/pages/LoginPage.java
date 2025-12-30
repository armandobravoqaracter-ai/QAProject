package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    
    private static final String PAGE_URL = "https://www.saucedemo.com/";
    
    // Localizadores
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isLoaded() {
        try {
            return isElementVisible(usernameField) 
                    && isElementVisible(passwordField)
                    && isElementVisible(loginButton)
                    && getCurrentUrl().equals(PAGE_URL);
        } catch (Exception e) {
            return false;
        }
    }

    public void goToPage() {
        navigateTo(PAGE_URL);
        if (!isLoaded()) {
            throw new RuntimeException("La página de login no se cargó correctamente");
        }
    }

    public InventoryPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new InventoryPage(driver);
    }

    public void enterUsername(String username) {
        typeText(usernameField, username);
    }

    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    public void clickLoginButton() {
        clickElement(loginButton);
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessage);
    }

    public String getErrorMessage() {
        return getElementText(errorMessage);
    }
    
    public boolean isLoginButtonVisible() {
        return isElementVisible(loginButton);
    }
}