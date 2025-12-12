package com.stepdefinitions;

import com.pages.RestPage;
import io.cucumber.java.es.*;
import io.restassured.response.Response;

import java.util.Map;

public class ApiSteps {

    RestPage rest = new RestPage();
    Response respuesta;

    @Dado("^El usuario agrega los headers a la petición:")
    public void que_agrego_los_headers(Map<String,String> headers) {
        rest.setHeaders(headers);
    }

    @Cuando("^El usuario realiza una petición GET a la url (.+?)")
    public void realizo_peticion_get(String url) {
        respuesta = rest.get(url);
    }

    @Entonces("^El usuario valida el código de respuesta debe ser (.+?)")
    public void validar_codigo(int codigo) {
        rest.validarStatusCode(respuesta, codigo);
    }

    @Entonces("^El usuario valida el campo (.+?) debe ser (.+?)")
    public void validar_campo(String campo, String valor) {
        rest.validarCampo(respuesta, campo, valor);
    }
}
