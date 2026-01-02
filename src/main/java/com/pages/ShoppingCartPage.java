package com.pages;

import com.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * Page Object para la página de Shopping Cart.
 */
public class ShoppingCartPage extends BasePage {

    private final By addSauceLabsBackpackButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By removeSauceLabsBackpackButton = By.id("remove-sauce-labs-backpack");
    private final By addSauceLabsBikeLightButton = By.id("add-to-cart-sauce-labs-bike-light");

    private final By itemPriceLocator = By.className("inventory_item_price");
    private final By cartTotalLocator = By.className("summary_total_label");
    
    // Componente reutilizable del header
    private final HeaderComponent header;

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }
    
    @Override
    public boolean isLoaded() {
        // ShoppingCartPage puede tener diferentes URLs (/cart.html, /checkout-step-one.html, etc.)
        return header.isCartVisible();
    }

    public void addSauceLabsBackpack() {
        driver.findElement(addSauceLabsBackpackButton).click();
    }

    public void addSauceLabsBikeLight() {
        driver.findElement(addSauceLabsBikeLightButton).click();
    }

    public void removeSauceLabsBackpack() {
        driver.findElement(removeSauceLabsBackpackButton).click();
    }

    // Métodos adicionales
    public void clickCartIcon() {
        header.goToCart();
    }

    public boolean areChargesAccurate() {
        clickCartIcon();
        double calculatedTotal = calculateTotalPrice();
        double displayedTotal = getDisplayedCartTotal();
        return calculatedTotal == displayedTotal;
    }

    /**
     * E3: Convierte nombre de producto a slug.
     */
    private String productNameToSlug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }
    
    /**
     * E3: Validación de precio mejorada - usa clase CSS estable en lugar de text().
     */
    public boolean isPriceCorrect(String productPrice) {
        // E3: Usar clase CSS específica en lugar de XPath con text()
        By productPriceLocator = By.cssSelector(".cart_item .inventory_item_price");
        try {
            WebElement priceElement = waitForElementToBeVisible(productPriceLocator);
            return priceElement.getText().contains(productPrice);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areProductsListedOnCheckout() {
        clickCartIcon();
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        return cartItems.size() > 0; // Verifica que haya productos en el carrito
    }

    public boolean areCheckoutFieldsEnabled() {
        By firstNameField = By.id("first-name");
        By lastNameField = By.id("last-name");
        By postalCodeField = By.id("postal-code");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeField));

        return driver.findElement(firstNameField).isEnabled()
                && driver.findElement(lastNameField).isEnabled()
                && driver.findElement(postalCodeField).isEnabled();
    }

    public double calculateTotalPrice() {
        double total = 0.0;
        List<WebElement> prices = driver.findElements(itemPriceLocator);
        for (WebElement priceElement : prices) {
            String priceText = priceElement.getText().replace("$", "").trim();
            total += Double.parseDouble(priceText);
        }
        return total;
    }

    public double getDisplayedCartTotal() {
        String totalText = driver.findElement(cartTotalLocator).getText().replace("Total: $", "").trim();
        return Double.parseDouble(totalText);
    }

    public void clickCheckoutButton() {
        By checkoutButton = By.id("checkout");
        driver.findElement(checkoutButton).click();
    }

    public int getCartItemCount() {
        return header.getCartItemCount();
    }

    public boolean areProductPricesCorrect() {
        clickCartIcon();
        List<WebElement> itemPrices = driver.findElements(itemPriceLocator);

        for (WebElement priceElement : itemPrices) {
            if (priceElement.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}