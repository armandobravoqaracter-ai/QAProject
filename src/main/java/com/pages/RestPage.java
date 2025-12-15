package com.pages;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.Assert;

import java.util.Map;

public class RestPage {

    private RequestSpecification request;

    public RestPage() {
        this.request = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .setRelaxedHTTPSValidation()
                .build();
    }

    /**
     * Permite agregar headers dinámicos desde los StepDefs
     */
    public void setHeaders(Map<String, String> headers) {
        this.request = request.headers(headers);
    }

    /**
     * Ejecuta el GET y regresa el response.
     */
    public Response get(String url) {
        return RestAssured
                .given()
                .spec(request)
                .log().all()
                .when()
                .get(url)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /**
     * Validar el código HTTP
     */
    public void validarStatusCode(Response response, int statusEsperado) {
        Assert.assertEquals(response.statusCode(), statusEsperado,
                "El código de respuesta no coincide");
    }

    /**
     * Validar un campo dentro del JSON (nivel simple)
     */
    public void validarCampo(Response respuesta, String campo, String valorEsperado) {
        Object valorReal = respuesta.jsonPath().get(campo);

        // Normalizar tipo
        if (valorReal instanceof Number) {
            Assert.assertEquals(
                    Integer.valueOf(valorEsperado),
                    ((Number) valorReal).intValue(),
                    "El campo '" + campo + "' no coincide."
            );
        } else {
            Assert.assertEquals(
                    valorReal.toString(),
                    valorEsperado,
                    "El campo '" + campo + "' no coincide."
            );
        }
    }

}
