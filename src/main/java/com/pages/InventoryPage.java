package com.pages;

import com.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object para la página de Inventario de Saucedemo.
 */
public class InventoryPage extends BasePage {
    
    private static final String PAGE_URL = "https://www.saucedemo.com/inventory.html";
    
    private final By inventoryContainer = By.id("inventory_container");
    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    
    // Componente reutilizable del header
    private final HeaderComponent header;
    
    /**
     * E3: Convierte nombre de producto a slug para data-test selector.
     */
    private String productNameToSlug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }
    
    // E3: Localizadores dinámicos refactorizados - CSS con data-test
    private By getAddToCartButton(String productName) {
        String slug = productNameToSlug(productName);
        return By.cssSelector("[data-test='add-to-cart-" + slug + "']");
    }
    
    private By getRemoveButton(String productName) {
        String slug = productNameToSlug(productName);
        return By.cssSelector("[data-test='remove-" + slug + "']");
    }
    
    public InventoryPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
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
        wait.until(driver -> header.getCartItemCount() == expectedCount);
    }
    
    public void removeProductFromCart(String productName) {
        clickElement(getRemoveButton(productName));
    }
    
    // Delegación al HeaderComponent
    public int getCartItemCount() {
        return header.getCartItemCount();
    }

    public void goToCart() {
        header.goToCart();
    }
    
    public void logout() {
        header.logout();
    }
    
    public boolean isMenuButtonVisible() {
        return header.isMenuButtonVisible();
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
}
