package com.stepdefinitions;

import com.hooks.BaseTest;
import com.pages.InventoryPage;
import com.pages.LoginPage;
import com.pages.ShoppingCartPage;
import com.utils.StabilityMetricsCollector;
import io.cucumber.java.After;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Step Definitions para E1 - Baseline de estabilidad.
 */
public class StabilityBaselineSteps {

    private final WebDriver driver;
    private static StabilityMetricsCollector metricsCollector;
    
    private static Map<Integer, Map<String, Boolean>> allRunResults = new HashMap<>();
    private int currentRun = 0;
    private long runStartTime;
    
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private ShoppingCartPage cartPage;
    private boolean testPassed;
    
    // Suite mínima definida
    private static final String GRANULAR_TEST = "Login granular";
    private static final String E2E_TEST = "Flujo E2E compra";
    
    public StabilityBaselineSteps(BaseTest baseTest) {
        this.driver = baseTest.getDriver();
        
        if (metricsCollector == null) {
            metricsCollector = new StabilityMetricsCollector("Suite Mínima - E1 Stability");
            System.out.println("   1. Test Granular: Login básico");
            System.out.println("   2. Test E2E: Login + Agregar producto + Validar carrito\n");
        }
    }

    @Dado("que tengo una suite mínima definida con {int} pruebas")
    public void queTengoUnaSuiteMinimaDefinidaConPruebas(int numPruebas) {
        Assert.assertEquals(numPruebas, 2, "La suite mínima debe tener 2 pruebas");
    }

    @Cuando("ejecuto la corrida número {int}")
    public void ejecutoLaCorridaNumero(int runNumber) {
        this.currentRun = runNumber;
        this.runStartTime = System.currentTimeMillis();
        
        allRunResults.putIfAbsent(runNumber, new HashMap<>());
        
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Ejecucion #" + runNumber + " de 10");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Y("ejecuto el test granular de login con {string} y {string}")
    public void ejecutoElTestGranularDeLoginConY(String usuario, String password) {
        System.out.println("\n Ejecutando: " + GRANULAR_TEST);
        
        try {
            driver.get("https://www.saucedemo.com/");
            loginPage = new LoginPage(driver);
            
            inventoryPage = loginPage.loginAs(usuario, password);
            
            testPassed = inventoryPage != null && inventoryPage.isLoaded();
            
            if (testPassed) {
                System.out.println("PASS - Login exitoso");
            } else {
                System.out.println("FAIL - Login falló");
            }
            
        } catch (Exception e) {
            testPassed = false;
            System.out.println("FAIL - Excepción: " + e.getMessage());
        }
    }

    @Entonces("el resultado de estabilidad debería ser {string}")
    public void elResultadoDeEstabilidadDeberiaSer(String expectedResult) {
        if ("SUCCESS".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(testPassed, "Se esperaba SUCCESS pero el test falló");
        } else {
            Assert.assertFalse(testPassed, "Se esperaba FAILURE pero el test pasó");
        }
    }

    @Y("registro el resultado de la corrida {int}")
    public void registroElResultadoDeLaCorrida(int runNumber) {
        allRunResults.get(runNumber).put(GRANULAR_TEST, testPassed);
        System.out.println("   📊 Resultado registrado para corrida #" + runNumber);
        
        verificarYRegistrarCorrida(runNumber);
    }

    @Y("ejecuto el test e2e: login, agregar producto y validar carrito")
    public void ejecutoElTestE2eLoginAgregarProductoYValidarCarrito() {
        System.out.println("\n🧪 Ejecutando: " + E2E_TEST);
        
        try {
            driver.get("https://www.saucedemo.com/");
            loginPage = new LoginPage(driver);
            inventoryPage = loginPage.loginAs("standard_user", "secret_sauce");
            
            Assert.assertNotNull(inventoryPage, "Login falló en test E2E");
            Assert.assertTrue(inventoryPage.isLoaded(), "Página de inventario no cargó");
            System.out.println("Paso 1: Login exitoso");
            
            String producto = "Sauce Labs Backpack";
            inventoryPage.addProductToCart(producto);
            // E2: Reemplazar Thread.sleep con wait explícito del contador
            inventoryPage.waitForCartCount(1); // Espera a que el contador se actualice
            System.out.println("  Paso 2: Producto agregado");
            
            int itemCount = inventoryPage.getCartItemCount();
            Assert.assertEquals(itemCount, 1, "El carrito debería tener 1 item");
            System.out.println("Paso 3: Carrito validado (1 item)");
            
            testPassed = true;
            System.out.println("PASA - Flujo E2E completado");
            
        } catch (Exception e) {
            testPassed = false;
            System.out.println("FAIL - Excepción en E2E: " + e.getMessage());
        }
    }

    @Entonces("el flujo e2e debería completarse exitosamente")
    public void elFlujoE2eDeberiaCompletarseExitosamente() {
        Assert.assertTrue(testPassed, "El flujo E2E no se completó exitosamente");
    }

    @Y("registro el resultado e2e de la corrida {int}")
    public void registroElResultadoE2eDeLaCorrida(int runNumber) {
        allRunResults.get(runNumber).put(E2E_TEST, testPassed);
        
        verificarYRegistrarCorrida(runNumber);
    }
    
    private void verificarYRegistrarCorrida(int runNumber) {
        Map<String, Boolean> runResults = allRunResults.get(runNumber);
        
        if (runResults.size() == 2) {
            long executionTime = System.currentTimeMillis() - runStartTime;
            metricsCollector.recordRun(runNumber, runResults, executionTime);
            
            long passedCount = runResults.values().stream().filter(p -> p).count();
            System.out.println("\n📊 Corrida #" + runNumber + " completada: " + 
                    passedCount + "/2 tests pasaron (" + (executionTime/1000.0) + "s)");
            
            // 10, generar el reporte final
            if (runNumber == 10 && allRunResults.size() == 10) {
                generarReporteFinal();
            }
        }
    }
    
    private void generarReporteFinal() {
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("GENERANDO REPORTE FINAL DE ESTABILIDAD");
        System.out.println("=".repeat(60));
        
        String report = metricsCollector.generateStabilityReport();
        System.out.println(report);
        
        String rootCause = metricsCollector.identifyRootCauseFocus();
        System.out.println("\n" + rootCause);
        
        try {
            String timestamp = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "target/stability-baseline-report-" + timestamp + ".txt";
            java.nio.file.Files.write(java.nio.file.Paths.get(fileName), 
                    (report + "\n\n" + rootCause).getBytes());
            System.out.println("\n Reporte guardado en: " + fileName);
        } catch (Exception e) {
            System.err.println("No se pudo guardar el reporte: " + e.getMessage());
        }
    }

    @After(value = "@E1_Stability", order = 100)
    public void limpiarMetricsCollector() {
        // Limpiar collector cuando termine toda la suite
        if (metricsCollector != null && metricsCollector.getTestRuns().size() >= 10) {
            metricsCollector = null;
        }
    }
}
