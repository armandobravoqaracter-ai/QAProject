package com.stepdefinitions;

import com.hooks.BaseTest;
import com.pages.InventoryPage;
import com.pages.LoginPage;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Step Definitions para el test E3 - Login con DataProvider.
 */
public class LoginDataDrivenSteps {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private InventoryPage inventoryPage;
    private String expectedOutcome;

    public LoginDataDrivenSteps(BaseTest baseTest) {
        this.driver = BaseTest.getDriver();
        this.loginPage = new LoginPage(driver);
    }

    @Entonces("el resultado debería ser {string}")
    public void elResultadoDeberiaSer(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
        System.out.println("⏳ Resultado esperado: " + expectedOutcome);
        
        if ("SUCCESS".equals(expectedOutcome)) {
            inventoryPage = new InventoryPage(driver);
            boolean loginSuccessful = inventoryPage.isLoaded();
            
            System.out.println("✅ RESULTADO: Login EXITOSO - Redirigió a inventario");
            
            Assert.assertTrue(loginSuccessful, 
                "El login debería haber sido exitoso pero falló");
                
        } else if ("ERROR".equals(expectedOutcome)) {
            boolean stillOnLoginPage = loginPage.isLoaded();
            
            System.out.println("⚠️  RESULTADO: Login FALLIDO (esperado)");
            
            Assert.assertTrue(stillOnLoginPage, 
                "Se esperaba permanecer en la página de login pero redirigió");
        }
    }

    @Y("si es ERROR debería mostrarse un mensaje de error")
    public void siEsErrorDeberiaMostrarseUnMensajeDeError() {
        if ("ERROR".equals(expectedOutcome)) {
            boolean errorDisplayed = loginPage.isErrorDisplayed();
            String errorMessage = errorDisplayed ? loginPage.getErrorMessage() : "Sin mensaje";
            
            System.out.println("   📋 Mensaje de error: " + errorMessage);
            
            Assert.assertTrue(errorDisplayed, 
                "Se esperaba un mensaje de error pero no se mostró");
            Assert.assertFalse(errorMessage.isEmpty(), 
                "El mensaje de error está vacío");
        } else {
            System.out.println("   ℹ️  No aplica validación de error (caso SUCCESS)");
        }
    }
}
