package com.stepdefinitions;

import com.hooks.BaseTest;
import com.pages.InventoryPage;
import com.pages.LoginPage;
import com.utils.CsvDataReader;
import com.utils.LoginTestData;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Datos externos desde CSV.
 */
public class LoginExternalDataSteps {

    private final WebDriver driver;
    private Map<String, LoginTestData> csvDataMap;
    private LoginTestData currentDataset;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private boolean loginSucceeded;
    private String errorMessage;
    
    public LoginExternalDataSteps(BaseTest baseTest) {
        this.driver = baseTest.getDriver();
    }

    @Dado("que los datos de prueba están cargados desde {string}")
    public void queLosDatosDePruebaEstanCargadosDesde(String csvPath) {

        List<LoginTestData> dataList;
        try {
            dataList = CsvDataReader.readLoginData(csvPath);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar datos desde CSV: " + csvPath, e);
        }
        
        System.out.println("Archivo CSV leído correctamente: " + csvPath);
        System.out.println("Total de datasets cargados: " + dataList.size());

        boolean allValid = dataList.stream().allMatch(LoginTestData::isValid);
        if (!allValid) {
            throw new IllegalStateException("Uno o más datasets en el CSV son inválidos");
        }

        csvDataMap = dataList.stream()
                .collect(Collectors.toMap(LoginTestData::getDatasetName, data -> data));
    }

    @Cuando("ejecuto el test con el dataset {string}")
    public void ejecutoElTestConElDataset(String datasetName) {
        currentDataset = csvDataMap.get(datasetName);
        
        if (currentDataset == null) {
            throw new IllegalArgumentException("Dataset no encontrado en CSV: " + datasetName);
        }

        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);

        try {
            inventoryPage = loginPage.loginAs(
                    currentDataset.getUsuario(),
                    currentDataset.getPassword()
            );

            if (inventoryPage != null && inventoryPage.isLoaded()) {
                loginSucceeded = true;
                errorMessage = null;
                System.out.println("Login EXITOSO - Redirigió a inventario");
            } else {
                loginSucceeded = false;
                errorMessage = loginPage.getErrorMessage();
                System.out.println("Login FALLÓ - Mensaje: " + errorMessage);
            }
        } catch (Exception e) {
            loginSucceeded = false;
            if (loginPage.isErrorDisplayed()) {
                errorMessage = loginPage.getErrorMessage();
                System.out.println("Login FALLÓ - Mensaje: " + errorMessage);
            } else {
                errorMessage = e.getMessage();
                System.out.println("Error inesperado: " + errorMessage);
            }
        }
    }
    
    @Entonces("el resultado del login desde CSV debería ser {string}")
    public void elResultadoDelLoginDesdeCsvDeberiaSer(String expectedOutcome) {
        
        if ("SUCCESS".equalsIgnoreCase(expectedOutcome)) {
            Assert.assertTrue(loginSucceeded,
                    " Se esperaba SUCCESS pero el login falló. Error: " + errorMessage);
            System.out.println(" Validación correcta: Login exitoso como se esperaba");
        } else if ("ERROR".equalsIgnoreCase(expectedOutcome)) {
            Assert.assertFalse(loginSucceeded,
                    "Se esperaba ERROR pero el login fue exitoso");
            System.out.println(" Validación correcta: Login falló como se esperaba");
        } else {
            throw new IllegalArgumentException("Outcome inválido: " + expectedOutcome);
        }
    }

    @Y("desde CSV si es éxito debería redirigir al inventario")
    public void desdeCsvSiEsExitoDeberiaRedirigirAlInventario() {
        if (loginSucceeded) {
            Assert.assertNotNull(inventoryPage, "InventoryPage no debería ser null");
            Assert.assertTrue(inventoryPage.isLoaded(),
                    "La página de inventario no está cargada correctamente");
            System.out.println("Validación correcta: Página de inventario cargada");
        }
    }

    @Y("desde CSV si es error debería mostrarse un mensaje de error")
    public void desdeCsvSiEsErrorDeberiaMostrarseUnMensajeDeError() {
        if (!loginSucceeded) {
            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Debería mostrarse un mensaje de error");
            Assert.assertNotNull(errorMessage, "El mensaje de error no debería ser null");
            Assert.assertFalse(errorMessage.isEmpty(), "El mensaje de error no debería estar vacío");
            System.out.println(" Validación correcta: Mensaje de error mostrado");
        }
    }
}
