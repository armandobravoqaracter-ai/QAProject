package runners;

import com.hooks.BaseTest;
import com.pages.InventoryPage;
import com.pages.LoginPage;
import com.utils.CsvDataReader;
import com.utils.LoginTestData;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.List;

/**
 * E4 - Test con DataProvider alimentado desde archivo CSV externo.
 */
public class LoginExternalDataTest {

    private WebDriver driver;
    private LoginPage loginPage;
    
    private static List<LoginTestData> cachedTestData;

    @BeforeClass
    public void setUpClass() {
        String browser = System.getProperty("SELENIUM_GRID_BROWSER", "edge").toLowerCase();
        
        switch (browser) {
            case "edge":
                WebDriverManager.edgedriver().setup();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                break;
        }
        
        try {
            cachedTestData = CsvDataReader.readLoginData("testdata/login_datasets.csv");
            
        } catch (IOException e) {
            Assert.fail("❌ Error al cargar archivo CSV: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Assert.fail("❌ Datos inválidos en CSV: " + e.getMessage());
        }
    }

    @BeforeMethod
    public void setUp() {
        String browser = System.getProperty("SELENIUM_GRID_BROWSER", "edge").toLowerCase();
        
        switch (browser) {
            case "edge":
                driver = new EdgeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                driver = new ChromeDriver();
                break;
        }
        
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "loginDataFromCsv")
    public Object[][] loginDataFromCsv() {
        return CsvDataReader.toDataProviderArray(cachedTestData);
    }

    @Test(dataProvider = "loginDataFromCsv", 
          description = "E4 - Test de login con datos externos (CSV)",
          groups = {"E4", "granular", "login", "external-data"})
    public void testLoginWithExternalData(String datasetName, String usuario, String password, 
                                          String expectedOutcome, String nota) {
        
        loginPage.goToPage();
        Assert.assertTrue(loginPage.isLoaded(), 
            "[" + datasetName + "] La página de login no se cargó correctamente");
        
        loginPage.enterUsername(usuario);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        
        if ("SUCCESS".equals(expectedOutcome)) {
            InventoryPage inventoryPage = new InventoryPage(driver);
            
            boolean loginSuccessful = inventoryPage.isLoaded();
            
            Assert.assertTrue(loginSuccessful, 
                "[" + datasetName + "] El login debería haber sido exitoso pero falló");
            
        } else if ("ERROR".equals(expectedOutcome)) {
            boolean errorDisplayed = loginPage.isErrorDisplayed();
            String errorMessage = errorDisplayed ? loginPage.getErrorMessage() : "Sin mensaje de error";
          
            Assert.assertTrue(errorDisplayed, 
                "[" + datasetName + "] Se esperaba un mensaje de error pero no se mostró");
            Assert.assertFalse(errorMessage.isEmpty(), 
                "[" + datasetName + "] El mensaje de error está vacío");
            
        } else {
            Assert.fail("[" + datasetName + "] expectedOutcome inválido: " + expectedOutcome);
        }
        
        System.out.println("Dataset '" + datasetName + "' completado exitosamente\n");
    }
}
