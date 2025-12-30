package com.components;

import com.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Componente reutilizable para el Header de Saucedemo.
 */
public class HeaderComponent extends BasePage {
    
    // Localizadores del header
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.id("shopping_cart_container");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    
    public HeaderComponent(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isLoaded() {
        return isElementVisible(menuButton) && isElementVisible(shoppingCartLink);
    }
    
    public int getCartItemCount() {
        try {
            String badgeText = getElementText(shoppingCartBadge);
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public void goToCart() {
        clickElement(shoppingCartLink);
    }
    
    public void logout() {
        clickElement(menuButton);
        waitForElementToBeVisible(logoutLink);
        clickElement(logoutLink);
    }
    
    public boolean isMenuButtonVisible() {
        return isElementVisible(menuButton);
    }
    
    public boolean isCartVisible() {
        return isElementVisible(shoppingCartLink);
    }
}
