package com.stepdefinitions;

import com.hooks.BaseTest;
import com.pages.webPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.es.Dado;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

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
}