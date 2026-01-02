package com.pages;

import com.utils.WaitActions;
import lombok.Value;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.xpath.XPath;
import java.io.*;
import java.time.Duration;

public class webPage {

    protected final WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected Select select;
    protected WaitActions waitActions; // E2: Wrapper de waits

    public webPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.waitActions = new WaitActions(driver); // E2: Inicializar wrapper
        PageFactory.initElements(driver, this);
    }

    private void init() {
        PageFactory.initElements(driver, this);
    }
    public void abrirUrl(String url) {
        try {
            System.out.println("Abriendo URL:" + url);
            driver.get(url);
        } catch (Exception e) {
            System.out.println("No se puede abrir la URL: "+ url + e);
            throw e;
        }
    }
    public By initBy(String locator) {

        if (locator.startsWith("id:")) {
            locator = locator.replace("id:", "");
            return By.id(locator);
        }
        if (locator.startsWith("name:")) {
            locator = locator.replace("name:", "");
            return By.name(locator);
        }
        if (locator.startsWith("className:")) {
            locator = locator.replace("className:", "");
            return By.className(locator);
        }
        if (locator.startsWith("cssSelector:")) {
            locator = locator.replace("cssSelector:", "");
            return By.cssSelector(locator);
        }
        return By.xpath(locator);
    }
    public WebElement retryFindElement(By locator) {
        long startTime = System.currentTimeMillis(); // Marca el inicio
        try {
            WebElement element = wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.visibilityOfElementLocated(locator)));
            long endTime = System.currentTimeMillis(); // Marca el fin exitoso
            System.out.println("Tiempo transcurrido: " + (endTime - startTime) + " ms.");
            return element;
        } catch (TimeoutException e) {
            long endTime = System.currentTimeMillis(); // Marca el fin en caso de timeout
            System.out.println("El timeout ocurrió después de: " + (endTime - startTime) + " ms.");
            System.out.println("No se pudo encontrar el elemento con localizador en el tiempo de espera especificado. {}" + locator + e);
            return null;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis(); // Marca el fin en caso de otra excepción
            System.out.println("Ocurrió un error después de: " + (endTime - startTime) + " ms.");
            System.out.println("Error al intentar encontrar el elemento con localizador: " + locator + e);
            return null;
        }
    }
    private WebElement findElementSafely(By locator) {
        try {
            WebElement element = retryFindElement(locator);
            if (element == null) {
                System.out.println("No se pudo encontrar el elemento con localizador: "+locator);
                throw new IllegalStateException("No se pudo encontrar el elemento con localizador: " + locator);
            }
            return element;
        } catch (NoSuchElementException | InvalidSelectorException | TimeoutException | StaleElementReferenceException e) {
            System.out.println("Error al intentar encontrar el elemento con localizador : " + locator + e);
            throw e;
        } catch (Exception e) {
            System.out.println("Error al intentar encontrar el elemento con localizador : " + locator + e);
            throw e; // Re-lanzar la excepción permite a los llamadores manejarla según sea necesario.
        }
    }
    public void clickWebElement(By locator) {
        //WebElement element = wait.until(ExpectedConditions.elementToBeClickable(findElementSafely(locator)));
        WebElement element = findElementSafely(locator);
        try {
            // Intertar hacer scroll al elemento y centrarlo en la pantalla
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            actions.moveToElement(element).click().perform();
            System.out.println("Elemento cliqueado exitosamente con click intentando con Actions: "+ element);
        } catch (Exception exception_webelement) {
            System.out.println("Fallo el click intentando con Actions en el elemento, intentando con directo: "+element + exception_webelement);
            try {
                actions.moveToElement(element);
                element.click();
                System.out.println("Elemento cliqueado exitosamente con Actions: "+ element);
            } catch (NoSuchElementException | InvalidSelectorException | TimeoutException | StaleElementReferenceException e) {
                System.out.println("No se pudo encontrar el elemento con localizador en el tiempo de espera especificado."+ locator);
                throw e;
            } catch (Exception exception_clic) {
                System.out.println("No se pudo clickear el elemento incluso con Actions: "+element + exception_clic);
                throw new RuntimeException("No se puede clickear el elemento: " + locator, exception_clic);
            }
        }
    }
    public void CompareURL(String url) {
        try {
            String url_actual = driver.getCurrentUrl();
            System.out.println("URL Actual: "+ url_actual);
            if (url_actual.equals(url)) {
                System.out.println("URL Valida"+ url_actual);
            } else {
                System.out.println("No es la misma URL: que "+ url_actual + url);
            }
        } catch (Exception e) {
            System.out.println("No se puede extraer la URL:"+ url+ e);
            throw e;
        }
    }
    public void focusOnWebElementNoClick(By by) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            actions.moveToElement(element).perform();
        } catch (NoSuchElementException e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                actions.moveToElement(element).perform();
            } catch (NoSuchElementException i) {
                throw new IllegalStateException("No se encontro el elemento " + by.toString() + "para enfocarlo.");
            }

        }
    }
    public void doAWait(int time) {
        synchronized (this) {
            try {
                System.out.println("Generando tiempo de espera " + time);
                wait(time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
            }
        }
    }
    public void clearsendKeysToElemnt(By locator, String text) {

        String browser = System.getProperty("browser", "firefox");

        WebElement element = findElementSafely(locator);
        try {
            if (browser.equalsIgnoreCase("firefox")) {
                System.out.println("Browser: firefox");
                element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
                element.sendKeys(text);
                element.sendKeys(Keys.TAB);
            } else {
                System.out.println("Browser: NO firefox");
                element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), text, Keys.TAB);
            }
        } catch (Exception e) {
            System.out.println("No se puede escribir en: {}" + element + e);
            throw e;
        }
    }
    public String GetTextWebElement(By locator) {
        WebElement element = findElementSafely(locator);
        try {
            String texto_extraido = element.getText().trim();
            System.out.println("Texto extraido: {}"+texto_extraido);
            return texto_extraido;
        } catch (Exception e) {
            System.out.println("No se puede extraer el texto"+ e);
            return null;
        }
    }
    public void validarTexto(By locator, String textoavalidar) {
        String texto = GetTextWebElement(locator);
        try {
            if (texto.trim().equals(textoavalidar.trim())) {
                System.out.println("Texto es igual: "+ texto);
            } else {
                System.out.println("No se igual el texto: "+ texto);
            }
        } catch (Exception e) {
            System.out.println("No se puede extraer el texto"+ texto);
            //throw e;
        }
    }
    public void validarTextoVacio(By locator) {
        WebElement element = findElementSafely(locator);
        try {
            if (element.getAttribute("value").isEmpty()) {
                System.out.println("El elemento con Atributo value esta Vacio");
            } else if (element.getText().isEmpty() || element.getText().isBlank()) {
                System.out.println("El Texti del elemento es nulo o vacio");
            } else {
                System.out.println("El no esta vacio y contiene texto");
            }
        } catch (TimeoutException e) {
            throw e;
        }
    }
    public Select select(WebElement element) {
        return select = new Select(element);
    }
    public void selectTextWebElement(By locator, String text) {

        // E2: Reemplazar Thread.sleep con waitClickable
        WebElement element = waitActions.waitClickable(locator);
        try {
            select(element).selectByVisibleText(text);
        } catch (Exception e) {
            System.out.println("no se puede seleccionar el texto: en el elemento"+ text+ e);
            System.out.println("No se pudo seleccionar: , en el combo: "+ text+ locator);
            System.out.println(e.getMessage());
            throw e;
        }

    }
    public String ExtraerValorDelArchivo(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea =  br.readLine();
        return linea;
    }
    public void extractValueOfElementAndSaveInFile(By locator, String ruta) throws IOException {

        WebElement elemento = driver.findElement(locator);

        String valorSeleccionado = "";

        String value = elemento.getAttribute("value");
        if (value != null && !value.trim().isEmpty()) {
            valorSeleccionado = value.trim();
        }

        else {
            String innerText = elemento.getAttribute("innerText");
            if (innerText != null && !innerText.trim().isEmpty()) {
                valorSeleccionado = innerText.trim();
            }
            else {

                String textContent = elemento.getAttribute("textContent");
                if (textContent != null && !textContent.trim().isEmpty()) {
                    valorSeleccionado = textContent.trim();
                }

                else {
                    String text = elemento.getText();
                    if (text != null) {
                        valorSeleccionado = text.trim();
                    }
                }
            }
        }

        // Nunca será null — si todo falló, será "".
        System.out.println("Valor extraído: [" + valorSeleccionado + "]");

        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.write(valorSeleccionado);
        }
    }
}
