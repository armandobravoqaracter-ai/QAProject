package com.stepdefinitions;

import com.hooks.BaseTest;
import com.pages.InventoryPage;
import com.pages.LoginPage;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Step Definitions para el test E2E de Login + Inventario (E1).
 */
public class LoginInventoryE2ESteps {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private InventoryPage inventoryPage;

    public LoginInventoryE2ESteps(BaseTest baseTest) {
        this.driver = baseTest.getDriver();
        this.loginPage = new LoginPage(driver);
    }

    @Cuando("el usuario realiza login con credenciales válidas")
    public void elUsuarioRealizaLoginConCredencialesValidas() {
        inventoryPage = loginPage.loginAs("standard_user", "secret_sauce");
    }

    @Cuando("el usuario intenta hacer login con usuario {string} y contraseña {string}")
    public void elUsuarioIntentaHacerLoginConUsuarioYContrasena(String usuario, String contrasena) {
        inventoryPage = loginPage.loginAs(usuario, contrasena);
    }

    @Entonces("el usuario debería estar en la página de inventario")
    public void elUsuarioDeberiaEstarEnLaPaginaDeInventario() {
        Assert.assertTrue(inventoryPage.isLoaded(), 
            "No se redirigió correctamente a la página de inventario");
    }

    @Y("el título de la página debería ser {string}")
    public void elTituloDeLaPaginaDeberiaSer(String tituloEsperado) {
        String tituloActual = inventoryPage.getTitle();
        Assert.assertEquals(tituloActual, tituloEsperado, 
            "El título de la página no es el esperado");
    }

    @Y("debería haber productos disponibles en el inventario")
    public void deberiaHaberProductosDisponiblesEnElInventario() {
        int cantidadProductos = inventoryPage.getProductCount();
        Assert.assertTrue(cantidadProductos > 0, 
            "No hay productos disponibles en el inventario");
        System.out.println("✓ Productos disponibles: " + cantidadProductos);
    }

    @Cuando("el usuario agrega el producto {string} al carrito")
    public void elUsuarioAgregaElProductoAlCarrito(String nombreProducto) {
        inventoryPage.addProductToCart(nombreProducto);
        inventoryPage.waitForCartCount(1);
    }

    @Entonces("el carrito debería mostrar {int} producto(s)")
    public void elCarritoDeberiaMostrarProductos(int cantidadEsperada) {
        int cantidadActual = inventoryPage.getCartItemCount();
        Assert.assertEquals(cantidadActual, cantidadEsperada, 
            "La cantidad de productos en el carrito no coincide");
        System.out.println("✓ Carrito con " + cantidadActual + " producto(s)");
    }

    @Y("el producto {string} debería estar en el carrito")
    public void elProductoDeberiaEstarEnElCarrito(String nombreProducto) {
        Assert.assertTrue(inventoryPage.isProductInCart(nombreProducto),
            "El producto '" + nombreProducto + "' no está marcado como agregado al carrito");
    }

    @Entonces("debería mostrarse un mensaje de error")
    public void deberiaMostrarseUnMensajeDeError() {
        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "No se muestra mensaje de error con credenciales inválidas");
    }

    @Y("el mensaje de error debería contener {string}")
    public void elMensajeDeErrorDeberiaContener(String textoEsperado) {
        String mensajeError = loginPage.getErrorMessage();
        Assert.assertTrue(mensajeError.contains(textoEsperado),
            "El mensaje de error no contiene el texto esperado. Actual: " + mensajeError);
        System.out.println("✓ Mensaje de error validado correctamente");
    }
}
