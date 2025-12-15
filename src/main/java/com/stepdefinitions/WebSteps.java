package com.stepdefinitions;

import com.hooks.BaseTest;
import com.pages.webPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.es.Dado;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.IOException;

public class WebSteps {

    private final WebDriver driver;
    private final webPage WebPage;

    public WebSteps(BaseTest baseTest) {
        this.driver = baseTest.getDriver();
        this.WebPage = new webPage(driver);
    }

    @Dado("^El usuario nava a la pagina web (.+?)")
    public void navegar(String url) {
        WebPage.abrirUrl(url);
    }

    @Dado("^El usuario valida que la pagina web actual sea (.+?)")
    public void validarUrl(String url) {
        WebPage.CompareURL(url);
    }

    @Dado("^El usuario da click en el boton (.+?)")
    public void clickEnBoton(String localizador) {
        WebPage.clickWebElement(WebPage.initBy(localizador));
    }

    @Dado("^El usuario realiza un scroll sin click en el elemento (.+?)")
    public void scroll(String localizador) {
        WebPage.focusOnWebElementNoClick(WebPage.initBy(localizador));
    }

    @Dado("^El usuario realiza un espera de (.+?)")
    public void esperar(String tiempo) {
        WebPage.doAWait(Integer.parseInt(tiempo));
    }

    @Dado("^El usuario navega a la pagina web (.+?)")
    public void elUsuarioNavegaALaPaginaWeb(String url) {
        WebPage.abrirUrl(url);
    }

    @Dado("^El usuario escribe texto limpiando el cambo (.+?) (.+?)")
    public void elUsuarioEscribeTextoLimpiandoElCambo(String localizador, String texto) {
        WebPage.clearsendKeysToElemnt(WebPage.initBy(localizador), texto);
    }

    @Dado("^El usuario valida el texto en el campo (.+?) sea igual a (.+?)")
    public void elUsuarioValidaElTextoEnElCampoSeaIgualA(String localizador, String texto) {
        WebPage.validarTexto(WebPage.initBy(localizador), texto);
    }

    @Dado("^El usuario valida que el texto no este vacio (.+?)")
    public void elUsuarioValidaQueElTextoNoEsteVacio(String locator) {
        WebPage.validarTextoVacio(WebPage.initBy(locator));
    }

    @Dado("^El usuario selecciona la opcion (.+?) del combo (.+?)")
    public void elUsuarioSeleccionaLaOpcionDelCombo(String opcion, String locator) {
        System.out.println("=====> elUsuarioSeleccionaLaOpcionDelCombo");
        System.out.println("=====> opcion: " + opcion );
        WebPage.selectTextWebElement(WebPage.initBy(locator), opcion);
    }

    @Dado("^El usuario extrae el texto del campo (.+?) y lo almacena en un archivo (.+?)")
    public void elUsuarioExtraeElTextoDelCampoYLoAlmacenaEnUnArchivo(String locator, String ruta) throws IOException {
        WebPage.extractValueOfElementAndSaveInFile(WebPage.initBy(locator), ruta);
    }

    @Dado("^El usuario extrae el valor del elemento y lo guarda en un archivo (.+?) (.+?)")
    public void elUsuarioExtraeElValorDelElementoYLoGuardaEnUnArchivo(String locator, String ruta) throws IOException {
        WebPage.selectTextWebElement(WebPage.initBy(locator),WebPage.ExtraerValorDelArchivo(ruta));
    }
}