package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object para la página de Inventario (Productos) de Saucedemo.
 * Modela acciones semánticas del usuario en la página de productos.
 */
public class InventoryPage extends BasePage {
    
    private static final String PAGE_URL = "https://www.saucedemo.com/inventory.html";
    
    private final By inventoryContainer = By.id("inventory_container");
    private final By pageTitle = By.className("title");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.id("shopping_cart_container");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By inventoryItems = By.className("inventory_item");
    
    // Localizadores dinámicos
    private By getAddToCartButton(String productName) {
        return By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[contains(@id, 'add-to-cart')]");
    }
    
    private By getRemoveButton(String productName) {
        return By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[contains(@id, 'remove')]");
    }
    
    public InventoryPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isLoaded() {
        try {
            return isElementVisible(inventoryContainer) 
                    && isElementVisible(pageTitle)
                    && getCurrentUrl().equals(PAGE_URL);
        } catch (Exception e) {
            return false;
        }
    }
    
    public void goToPage() {
        navigateTo(PAGE_URL);
        if (!isLoaded()) {
            throw new RuntimeException("La página de inventario no se cargó correctamente. ¿Usuario autenticado?");
        }
    }
    
    public String getTitle() {
        return getElementText(pageTitle);
    }
    
    public void addProductToCart(String productName) {
        clickElementWithJS(getAddToCartButton(productName));
    }
    
    public void waitForCartCount(int expectedCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(driver -> getCartItemCount() == expectedCount);
    }
    
    public void removeProductFromCart(String productName) {
        clickElement(getRemoveButton(productName));
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
    
    public boolean isProductAvailableToAdd(String productName) {
        return isElementVisible(getAddToCartButton(productName));
    }
    
    public boolean isProductInCart(String productName) {
        return isElementVisible(getRemoveButton(productName));
    }

    public int getProductCount() {
        return driver.findElements(inventoryItems).size();
    }
    
    public void logout() {
        clickElement(menuButton);
        waitForElementToBeVisible(logoutLink);
        clickElement(logoutLink);
    }
    
    public boolean isMenuButtonVisible() {
        return isElementVisible(menuButton);
    }
}
