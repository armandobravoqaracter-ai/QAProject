package com.stepdefinitions;

import com.pages.DbPage;
import io.cucumber.java.es.*;

public class DbSteps {

    DbPage dbPage = new DbPage();

    @Dado("^El usuario se conecta a PostgreSQL con host (.+?), puerto (.+?), bd (.+?), usuario (.+?) y password (.+?)")
    public void conectar_postgres(
            String host,
            String port,
            String database,
            String user,
            String password
    ) {
        dbPage.conectarDB(host, port, database, user, password);
    }

    @Cuando("^El usuario ejecuta la consulta SQL (.+?)")
    public void ejecutar_consulta_sql(String query) {
        dbPage.ejecutarConsulta(query);
    }

    @Entonces("^El usuario valida que el campo (.+?) sea (.+?)")
    public void validar_campo(String columna, String valorEsperado) {
        dbPage.validarCampo(columna, valorEsperado);
    }
}
